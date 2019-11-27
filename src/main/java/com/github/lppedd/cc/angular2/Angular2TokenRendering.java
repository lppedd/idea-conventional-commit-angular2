package com.github.lppedd.cc.angular2;

import com.github.lppedd.cc.api.CommitTokenElement;
import com.github.lppedd.cc.api.CommitTokenElement.CommitTokenRendering;

/**
 * @author Edoardo Luppi
 */
class Angular2TokenRendering {
  static final CommitTokenRendering SIMPLE = new CommitTokenRendering(
      false,
      false,
      false,
      CommitTokenElement.FOREGROUND,
      null,
      CCAngularIcons.ANGULAR2
  );

  static final CommitTokenRendering NG_MODULE = new CommitTokenRendering(
      false,
      false,
      false,
      CommitTokenElement.FOREGROUND,
      "NgModule",
      CCAngularIcons.ANGULAR2
  );
}
