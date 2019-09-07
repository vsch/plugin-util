# Plugin Util Version History

[TOC]: #

### Table of Contents
- [Version 1.1.0](#version-110)
- [Version 1.0.9](#version-109)
- [Version 1.0.8](#version-108)
- [Version 1.0.7](#version-107)
- [Version 1.0.6](#version-106)
- [Version 1.0.5](#version-105)
- [Version 1.0.3](#version-103)
- [Version 1.0.2](#version-102)
- [Version 1.0.1](#version-101)
- [Version 1.0.0](#version-100)


### Version 1.1.0

* Add: image transforms and drawing transforms primitives

### Version 1.0.9

* Fix: update to flexmark-java 0.50.40
* Fix: update to flexmark-java 0.50.38

### Version 1.0.8

* Add: `LineHighlightProvider.addHighlightLines(boolean, int[])` and
  `LineHighlightProvider.addHighlightLines(boolean, int[])` methods to add an array of lines
* Add: `LineHighlightProvider.addHighlightLine(int, boolean)` method to add lines only if there
  are not already in the highlight set.

### Version 1.0.7

* Fix: update to flexmark-java 0.50.34

### Version 1.0.6

* Fix: update to flexmark-java 0.50.30

### Version 1.0.5

* Fix: update to flexmark-java 0.50.20
* Add: `isAppVersionEqualOrGreaterThan` to replace `isAppVersionGreaterThan` which was really
  comparing versions >=.
* Deprecate: `isAppVersionGreaterThan`

### Version 1.0.3

* Fix: update to flexmark-java 0.50.18

### Version 1.0.2

* Fix: Version to take int[] for version parts.
* Fix: AppUtils version comparison when build numbers have less than 3 parts.
* Fix: move tree iteration utilities to separate project.

### Version 1.0.1

* Move tree iteration utilities to own library
* Move tree iteration utilities to own library

### Version 1.0.0

* Initial release version

