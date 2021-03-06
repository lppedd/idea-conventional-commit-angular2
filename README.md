<img align="left" width="85" height="85" src="https://raw.githubusercontent.com/lppedd/idea-conventional-commit-angular2/master/images/cc_angular_logo.png" alt="Plugin logo">

# Angular Conventional Commit

### Available @ [JetBrains Plugins Repository][4]

Looking for the latest **plugin binaries**? Get them [here][1] as `.zip`  
<small>Supported IDE versions: `192.*` to `203.*` (both inclusive)</small> 

-----

This plugin extends the functionality of [Conventional Commit][2] in Angular (2+) contexts.

Angular [commit message guidelines][3] are followed to provide additional commit _types_,
and projects are searched for `NgModule` annotated classes to provide additional commit `scopes`.

<br />
<img src="https://raw.githubusercontent.com/lppedd/idea-conventional-commit-angular2/master/images/ngmodules.png" alt="NgModule scopes">

-----

## Why Java and not Kotlin?

The answer to that question is straightforward: **bundle size**.  
I want to keep Conventional Commit extensions as small as possible.

## Author

 - Edoardo Luppi (<lp.edoardo@gmail.com>)

[1]: https://github.com/lppedd/idea-conventional-commit-angular2/releases
[2]: https://github.com/lppedd/idea-conventional-commit
[3]: https://github.com/angular/angular/blob/master/CONTRIBUTING.md#-commit-message-guidelines
[4]: https://plugins.jetbrains.com/plugin/13405-angular-conventional-commit
