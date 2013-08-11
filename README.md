# Person Directory Addons
This project is about an open source collection of extensions to the Person Directory.

==========================================================================

## Project Information

* [About](http://unicon.github.io/person-directory-addons/)
* [Changelog](https://github.com/Unicon/person-directory-addons/blob/master/changelog.md) 
* [JavaDocs](http://unicon.github.com/person-directory-addons/apidocs/index.html)
* [Wiki](https://github.com/Unicon/person-directory-addons/wiki)

## Current version
`0.1`

## Build 

[![Build Status](https://travis-ci.org/Unicon/person-directory-addons.png)](https://travis-ci.org/Unicon/person-directory-addons)

You can build the project from source using the following Maven command:

```bash
$ mvn clean package
```

## Usage
Declare the project dependency in your Local CAS server `pom.xml` file as:
```xml
<dependency>
    <groupId>net.unicon.persondir</groupId>
    <artifactId>persondir-addons</artifactId>
    <version>${persondir-addons.version}</version>
</dependency>
```
