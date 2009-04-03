pkg = {
    "name"          : "jersey-core",
    "version"       : "1.0.3,0-0.1",
    "attributes"    : { "pkg.summary" : "Jersey Core, RESTful Web services for GlassFish",
                        "pkg.description" : 
"Jersey is the open source (under dual CDDL+GPL license)\
 JAX-RS (JSR 311) Reference Implementation for building RESTful Web services. \
 But, it is also more than the Reference Implementation. \
 Jersey provides additional APIs and extension points (SPIs) \
 so that developers may extend Jersey to suite their needs. \
 This package contains Jersey core runtime libraries",
                     "info.classification" : "Web Services"  },
    "dirtrees" : [ "glassfish"],
    "depends" : { 
                  "pkg:/asm@3.1" : {"type" : "require" }
                  ,"pkg:/metro@1.4" : {"type" : "require" }
                  ,"pkg:/jettison@1.0.1" : {"type" : "require" }
                  ,"pkg:/jackson@0.9.4" : {"type" : "require" }
                },
    "licenses" : { "LICENSE.txt" : { "license" : "CDDL+GPL" }}
}
