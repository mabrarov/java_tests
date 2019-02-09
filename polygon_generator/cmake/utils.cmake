cmake_minimum_required(VERSION 2.8.11)

# Changes existing (default) compiler options.
# Parameters:
#   result - name of list to store compile options.
function(change_default_compile_options original_compile_options result)
    set(compile_options ${original_compile_options})
    # Turn on more strict warning mode
    if(MSVC)
        if(compile_options MATCHES "/W[0-4]")
            string(REGEX REPLACE "/W[0-4]" "/W4" compile_options "${compile_options}")
        else()
            set(compile_options "${compile_options} /W4")
        endif()
    endif()
    set(${result} "${compile_options}" PARENT_SCOPE)
endfunction()

# Changes existing (default) linker options.
# Parameters:
#   result - name of list to store link options.
function(change_default_link_options original_link_options result)
    set(link_options ${original_link_options})
    set(${result} "${link_options}" PARENT_SCOPE)
endfunction()

# Builds list of additional internal compiler options.
# Parameters:
#   result - name of list to store compile options.
function(config_private_compile_options result)
    set(compile_options )
    if(MSVC)
        # Turn on more strict warning mode
        list(APPEND compile_options "/W4")
        if((MSVC_VERSION EQUAL 1800) OR (MSVC_VERSION GREATER 1800))
            # Turn on option which is turned on by default in Visual Studio (when using Visual Studio generator)
            # ans is turned off by default in compiler command line (when using makefiles, for example).
            list(APPEND cxx_compile_options "/Zc:inline")
        endif()
    elseif(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
        list(APPEND compile_options "-Wall" "-Wextra" "-pedantic" "-Wunused" "-Wno-long-long")
    endif()
    set(${result} "${compile_options}" PARENT_SCOPE)
endfunction()

# Builds list of additional transitive compiler options.
# Parameters:
#   result - name of list to store compile options.
function(config_public_compile_options result)
    set(compile_options )
    # Turn on support of C++11 if it's available
    if(CMAKE_COMPILER_IS_GNUCXX)
        if(NOT (CMAKE_CXX_COMPILER_VERSION VERSION_LESS "4.7"))
            list(APPEND compile_options "-std=c++11")
        elseif(NOT (CMAKE_CXX_COMPILER_VERSION VERSION_LESS "4.3"))
            list(APPEND compile_options "-std=c++0x")
        endif()
    elseif(${CMAKE_CXX_COMPILER_ID} MATCHES "Clang")
        list(APPEND compile_options "-std=c++11")
    elseif(${CMAKE_CXX_COMPILER_ID} STREQUAL "Intel")
        if(NOT (CMAKE_CXX_COMPILER_VERSION VERSION_LESS "14"))
            list(APPEND compile_options "/Qstd=c++11")
        elseif(NOT (CMAKE_CXX_COMPILER_VERSION VERSION_LESS "12"))
            list(APPEND compile_options "/Qstd=c++0x")
        endif()
    endif()
    set(${result} "${compile_options}" PARENT_SCOPE)
endfunction()

# Builds list of additional internal compiler definitions.
# Parameters:
#   result - name of list to store compile definitions.
function(config_private_compile_definitions result)
    set(compile_definitions )
    if(MSVC)
        list(APPEND compile_definitions "_CRT_SECURE_NO_WARNINGS" "_CRT_NO_VA_START_VALIDATION")
    endif()
    set(${result} "${compile_definitions}" PARENT_SCOPE)
endfunction()

# Builds list of additional transitive compiler definitions.
# Parameters:
#   result - name of list to store compile definitions.
function(config_public_compile_definitions result)
    set(compile_definitions )
    # Additional preprocessor definitions for Windows target
    if(WIN32)
        list(APPEND compile_definitions
            WIN32
            WIN32_LEAN_AND_MEAN
            WINVER=0x0501
            _WIN32_WINNT=0x0501
            _WIN32_WINDOWS=0x0501
            _WIN32_IE=0x0600
            _WINSOCK_DEPRECATED_NO_WARNINGS)
    endif()
    set(${result} "${compile_definitions}" PARENT_SCOPE)
endfunction()
