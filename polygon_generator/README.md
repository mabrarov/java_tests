# Polygon Generator

Generator of random data for [Polygon](https://polygon.codeforces.com/).

## Usage

Windows Command Prompt

```
polygon_generator.exe [%number_of_generated_lines% [%number_of_unique_values% [%max_length_of_line_to_generate% [%min_length_of_line_to_generate%]]]]
```

Git Bash or *nix shell

```
./polygon_generator [${number_of_generated_lines} [%{number_of_unique_values} [${max_length_of_line_to_generate} [${min_length_of_line_to_generate}]]]]
```

### Parameters

Assuming `%` and `${..}` are removed:


| # | Name | Format | Default value | Meaning |
|---|---|---|---|---| 
| 1 | number_of_generated_lines | positive integer < 2 ^ 128 (on x64 systems) | 1000000 | total number of lines to be generated |
| 2 | number_of_unique_values | positive integer < 2 ^ 64 (on x64 systems) | 3/4 * number_of_generated_lines | number of unique lines to be generated |
| 3 | max_length_of_line_to_generate | positive integer < 2 ^ 64 (on x64 systems) | 100 | maximum length of the generated line |
| 4 | min_length_of_line_to_generate | positive integer < 2 ^ 64 (on x64 systems) | 0 | minimum length of the generated line |

## How to build

Assuming:

* current directory is build directory where CMake generated project files and built result will be placed
* `%polygon_generator_dir%` or `${polygon_generator_dir}` is `polygon_generator` directory of local copy of this git repository
* `%cmake_genarator%` or `${cmake_genarator}` is the name of [CMake generator](https://cmake.org/cmake/help/latest/manual/cmake-generators.7.html) to use:
  * `Visual Studio 12 2013 Win64` - Visual Studio 2013, x64 build with Visual C++
  * `Visual Studio 14 2015 Win64` - Visual Studio 2013, x64 build with Visual C++
  * `NMake Makefiles` - for building with NMake
  * `MinGW Makefiles` - for building with MinGW
  * `Unix Makefiles` - makefile for building on *nix
  * etc
* `%build_type%` or `${build_type}` is the [type of build](https://cmake.org/cmake/help/latest/variable/CMAKE_BUILD_TYPE.html).
  Requirement to specify type of build depends on used CMake generator.
  Most often used values:
  * `Release`
  * `Debug`

### Generating native project from CMake project

Dynamic C/C++ runtime, Windows Command Prompt and Visual Studio CMake generators:

```cmd
cmake -G %cmake_genarator% %polygon_generator_dir% 
```

Dynamic C/C++ runtime, Git Bash with MinGW or shell on *nix:

```bash
cmake -D CMAKE_BUILD_TYPE=${build_type} -G ${cmake_genarator} ${polygon_generator_dir} 
```

Static C/C++ runtime, Windows Command Prompt and Visual Studio CMake generators:

```cmd
cmake ^
-D CMAKE_USER_MAKE_RULES_OVERRIDE=%polygon_generator_dir%/cmake/static_c_runtime_overrides.cmake ^
-D CMAKE_USER_MAKE_RULES_OVERRIDE_CXX=%polygon_generator_dir%/cmake/static_cxx_runtime_overrides.cmake ^
-G %cmake_genarator% %polygon_generator_dir% 
```

Static C/C++ runtime, Git Bash with MinGW or shell on *nix:

```bash
cmake \
-D CMAKE_USER_MAKE_RULES_OVERRIDE=${polygon_generator_dir}/cmake/static_c_runtime_overrides.cmake \
-D CMAKE_USER_MAKE_RULES_OVERRIDE_CXX=${polygon_generator_dir}/cmake/static_cxx_runtime_overrides.cmake \
-D CMAKE_BUILD_TYPE=${build_type} -G ${cmake_genarator} ${polygon_generator_dir} 
```

### Building generated native project

Windows Command Prompt:

```cmd
cmake --build . --config %build_type%
```

Git Bash or shell on *nix:

```bash
cmake --build . --config ${build_type}
```

Location of built binary depends on used CMake generator.
