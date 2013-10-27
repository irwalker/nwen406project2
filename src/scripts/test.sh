#!/bin/sh
#
# Force Bourne Shell if not Sun Grid Engine default shell (you never know!)
#
#$ -S /bin/sh
#
# I know I have a directory here so I'll use it as my initial working directory
#
#$ -wd /vol/grid-solar/sgeusers/walkeriain 
#
if [ -z "$SGE_ARCH" ]; then
   echo "Can't determine SGE ARCH"
else
   if [ "$SGE_ARCH" = "lx-x86" ]; then
       JAVA_HOME="/usr/pkg/java/sun-6"
   fi
fi

if [ -z "$JAVA_HOME" ]; then
   echo "Can't define a JAVA_HOME"
else
   export JAVA_HOME
   PATH="/usr/pkg/java/bin:${JAVA_HOME}/bin:${PATH}"; export PATH

   java ScriptWrapper
fi
