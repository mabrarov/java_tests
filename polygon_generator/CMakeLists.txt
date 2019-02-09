cmake_minimum_required(VERSION 3.0)
project(polygon_generator)

# Additional helpers
include(cmake/utils.cmake)

# Get full list of build configurations
set(configuration_types ${CMAKE_CONFIGURATION_TYPES})
if(NOT configuration_types AND DEFINED CMAKE_BUILD_TYPE)
    list(APPEND configuration_types ${CMAKE_BUILD_TYPE})
endif()

# Common target properties (CMAKE_CXX_FLAGS)
change_default_compile_options("${CMAKE_CXX_FLAGS}" CMAKE_CXX_FLAGS)
foreach(configuration_type IN LISTS configuration_types)
    string(TOUPPER ${configuration_type} configuration_type_upper_case)
    set(configuration_cxx_flags CMAKE_CXX_FLAGS_${configuration_type_upper_case})
    change_default_compile_options("${${configuration_cxx_flags}}" ${configuration_cxx_flags})
endforeach(configuration_type)

# Common target properties (CMAKE_EXE_LINKER_FLAGS)
change_default_link_options("${CMAKE_EXE_LINKER_FLAGS}" CMAKE_EXE_LINKER_FLAGS)
foreach(configuration_type IN LISTS configuration_types)
    string(TOUPPER ${configuration_type} configuration_type_upper_case)
    set(configuration_cxx_flags CMAKE_EXE_LINKER_FLAGS_${configuration_type_upper_case})
    change_default_link_options("${${configuration_cxx_flags}}" ${configuration_cxx_flags})
endforeach(configuration_type)

config_public_compile_options(cxx_public_compile_options)
config_public_compile_definitions(cxx_public_compile_definitions)

config_private_compile_options(cxx_private_compile_options)
config_private_compile_definitions(cxx_private_compile_definitions)

add_executable(${PROJECT_NAME}
    "include/testlib.h"
    "src/main.cpp")
target_compile_options(${PROJECT_NAME}
    PUBLIC
    ${cxx_public_compile_options}
    PRIVATE
    ${cxx_private_compile_options})
target_compile_definitions(${PROJECT_NAME}
    PUBLIC
    ${cxx_public_compile_definitions}
    PRIVATE
    ${cxx_private_compile_definitions})
target_include_directories(${PROJECT_NAME}
    PUBLIC
    "include")
