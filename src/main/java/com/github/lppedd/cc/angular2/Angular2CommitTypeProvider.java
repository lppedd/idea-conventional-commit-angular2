package com.github.lppedd.cc.angular2;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.lppedd.cc.CCBundle;
import com.github.lppedd.cc.api.CommitTypeProvider;
import com.github.lppedd.cc.api.ProviderPresentation;

/**
 * @author Edoardo Luppi
 */
class Angular2CommitTypeProvider implements CommitTypeProvider {
  private static final List<CommitType> TYPES = Arrays.asList(
      new Angular2CommitType("refactor", CCBundle.get("commit.type.refactor")),
      new Angular2CommitType("fix", CCBundle.get("commit.type.fix")),
      new Angular2CommitType("feat", CCBundle.get("commit.type.feat")),
      new Angular2CommitType("perf", CCBundle.get("commit.type.perf")),
      new Angular2CommitType("test", CCBundle.get("commit.type.test")),
      new Angular2CommitType("style", CCBundle.get("commit.type.style")),
      new Angular2CommitType("build", CCBundle.get("commit.type.build")),
      new Angular2CommitType("docs", CCBundle.get("commit.type.docs")),
      new Angular2CommitType("ci", CCBundle.get("commit.type.ci"))
  );

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
  public List<CommitType> getCommitTypes(final String prefix) {
    return TYPES;
  }

  private static class Angular2CommitType extends CommitType {
    Angular2CommitType(
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
}
