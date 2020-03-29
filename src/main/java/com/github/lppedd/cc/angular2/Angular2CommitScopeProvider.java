package com.github.lppedd.cc.angular2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.angular2.entities.Angular2EntitiesProvider;
import org.angular2.entities.Angular2Entity;
import org.angular2.entities.Angular2Module;
import org.angular2.index.Angular2IndexingHandler;
import org.angular2.index.Angular2SourceModuleIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.lppedd.cc.api.CommitScope;
import com.github.lppedd.cc.api.CommitScopeProvider;
import com.github.lppedd.cc.api.ProviderPresentation;
import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.lang.javascript.psi.JSImplicitElementProvider;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.util.CachedValueProvider.Result;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.containers.ContainerUtil;

/**
 * @author Edoardo Luppi
 */
class Angular2CommitScopeProvider implements CommitScopeProvider {
  private static final List<CommitScope> SCOPES = Arrays.asList(
      new Angular2CommitBuildScope("npm", ""),
      new Angular2CommitBuildScope("gulp", ""),
      new Angular2CommitBuildScope("broccoli", "")
  );

  private static final Application APPLICATION = ApplicationManager.getApplication();
  private static final StubIndex STUB_INDEX = StubIndex.getInstance();

  private final Project project;
  private final CachedValuesManager cachedValuesManager;

  Angular2CommitScopeProvider(final Project project) {
    this.project = project;
    cachedValuesManager = CachedValuesManager.getManager(project);
  }

  @NotNull
  @Override
  public String getId() {
    return CCAngularConstants.PROVIDER_ID;
  }

  @NotNull
  @Override
  public ProviderPresentation getPresentation() {
    return CCAngularConstants.PRESENTATION;
  }

  @NotNull
  @Override
  public List<CommitScope> getCommitScopes(@Nullable final String commitType) {
    return "build".equals(commitType) ? SCOPES : findModules();
  }

  @NotNull
  private List<CommitScope> findModules() {
    return cachedValuesManager.getCachedValue(project, this::computeCommitScopes);
  }

  @NotNull
  private Result<List<CommitScope>> computeCommitScopes() {
    final var commitScopes =
        APPLICATION.runReadAction((Computable<List<CommitScope>>) () ->
            findSourceModules()
                .stream()
                .map(this::toCommitScope)
                .sorted(Comparator.comparing(CommitScope::getText))
                .collect(Collectors.toList())
        );

    return Result.create(commitScopes, PsiModificationTracker.MODIFICATION_COUNT);
  }

  /** Returns the local project's {@code NgModule}s, without externally provided ones. */
  @NotNull
  private Collection<Angular2Module> findSourceModules() {
    final Collection<Angular2Module> modules = new ArrayList<>(32);
    STUB_INDEX.processElements(
        Angular2SourceModuleIndex.KEY,
        Angular2IndexingHandler.NG_MODULE_INDEX_NAME,
        project,
        GlobalSearchScope.projectScope(project),
        JSImplicitElementProvider.class,
        elementProvider -> {
          if (elementProvider.isValid()) {
            final var module = Angular2EntitiesProvider.getModule(elementProvider);
            ContainerUtil.addIfNotNull(modules, module);
          }

          return true;
        });

    return modules;
  }

  /**
   * Transforms a {@code NgModule} definition to a {@link CommitScope}.<br />
   * If a JSDoc comment is found, it is used as scope description.
   */
  @NotNull
  private CommitScope toCommitScope(@NotNull final Angular2Entity module) {
    final var sourceElement = module.getTypeScriptClass();
    final var name = CCAngularUtils.toDashCase(module.getName()).replaceFirst("-module$", "");
    final var documentation =
        DocumentationManager
            .getProviderFromElement(sourceElement)
            .generateDoc(sourceElement, null);

    return new Angular2CommitScope(name, documentation != null ? documentation : "");
  }

  private static class Angular2CommitBuildScope extends CommitScope {
    Angular2CommitBuildScope(
        @NotNull final String text,
        @NotNull final String description) {
      super(text, description);
    }

    @NotNull
    @Override
    public CommitTokenRendering getRendering() {
      return Angular2TokenRendering.SIMPLE;
    }
  }

  private static class Angular2CommitScope extends CommitScope {
    Angular2CommitScope(
        @NotNull final String text,
        @NotNull final String description) {
      super(text, description);
    }

    @NotNull
    @Override
    public CommitTokenRendering getRendering() {
      return Angular2TokenRendering.NG_MODULE;
    }
  }
}
