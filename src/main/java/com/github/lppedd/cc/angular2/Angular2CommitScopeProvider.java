package com.github.lppedd.cc.angular2;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.angular2.lang.Angular2LangUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.lppedd.cc.api.CommitScopeProvider;
import com.github.lppedd.cc.api.ProviderPresentation;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.Processor;
import com.intellij.util.indexing.IdFilter;

/**
 * @author Edoardo Luppi
 */
class Angular2CommitScopeProvider implements CommitScopeProvider {
  private static final List<CommitScope> SCOPES = Arrays.asList(
      new Angular2CommitBuildScope("npm", null),
      new Angular2CommitBuildScope("gulp", null),
      new Angular2CommitBuildScope("broccoli", null)
  );

  private static final Application APPLICATION = ApplicationManager.getApplication();
  private final Project project;

  Angular2CommitScopeProvider(final Project project) {
    this.project = project;
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
  public List<CommitScope> getCommitScopes(final String commitType) {
    return "build".equals(commitType) ? SCOPES : findNgModules();
  }

  @NotNull
  private List<CommitScope> findNgModules() {
    final Collection<String> fileNames = new HashSet<>(64);

    final Processor<String> stringProcessor = fileName -> {
      if (fileName.toLowerCase().endsWith(".module.ts")) {
        fileNames.add(fileName);
      }

      return true;
    };

    final GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);

    APPLICATION.runReadAction(() ->
        FilenameIndex.processAllFileNames(
            stringProcessor,
            projectScope,
            IdFilter.getProjectIdFilter(project, false)
        )
    );

    return fileNames
        .stream()
        .flatMap(fileName ->
            APPLICATION.runReadAction((Computable<Collection<VirtualFile>>) () ->
                FilenameIndex.getVirtualFilesByName(
                    project,
                    fileName,
                    true,
                    projectScope
                )
            ).stream()
        )
        .map(virtualFile -> PsiUtilCore.getPsiFile(project, virtualFile))
        .filter(Angular2LangUtil::isAngular2Context)
        .map(PsiFileSystemItem::getName)
        .map(String::toLowerCase)
        .map(fileName -> fileName.replaceFirst(".module.ts$", ""))
        .sorted()
        .map(moduleName -> new Angular2CommitScope(moduleName, null))
        .collect(Collectors.toList());
  }

  private static class Angular2CommitBuildScope extends CommitScope {
    Angular2CommitBuildScope(
        @NotNull final String text,
        @Nullable final String description) {
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
        @Nullable final String description) {
      super(text, description);
    }

    @NotNull
    @Override
    public CommitTokenRendering getRendering() {
      return Angular2TokenRendering.NG_MODULE;
    }
  }
}
