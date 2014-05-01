//
// Copyright (c) 2014 ACI Worldwide
//
// Distributed under the Boost Software License, Version 1.0. (See accompanying
// file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)
//

/**
 * CLI parameters:
 *
 * [<number of generated lines> [<number of unique values> [<max length of line to generate> [<min length of line to generate>]]]]
 *
 * where
 *    <number of generated lines>      - total number of lines to be generated,
 *                                       the default is 1000000;
 *    <number of unique values>        - number of unique lines to be generated,
 *                                       the default is 3/4 * <number of generated lines>;
 *    <max length of line to generate> - maximum length of the generated line,
 *                                       the default is 100;
 *    <min length of line to generate> - minimum length of the generated line,
 *                                       the default is 0;  
 */

#if defined(WIN32)
#include <tchar.h>
#endif

#include <cstdlib>
#include <cstddef>
#include <string>
#include <vector>
#include <exception>
#include <iostream>

#include "testlib.h"

namespace {

template <typename T> 
T from_string(const std::string& s)
{
  std::istringstream stream(s);
  T temp;
  stream >> temp;
  return temp;
}

template <typename T> 
std::string to_string(const T& value)
{
  std::ostringstream stream;
  stream << value;
  return stream.str();
}

} // anonymous namespace

#if defined(WIN32)
int _tmain(int argc, _TCHAR* argv[])
#else
int main(int argc, char* argv[])
#endif
{
  typedef unsigned long long            max_uint_t;
  typedef std::vector<std::string>      string_vector;
  typedef string_vector::const_iterator string_vector_iterator;

  try
  {        
    registerGen(argc, argv, 1);

    // Parse CLI parameters
    const max_uint_t line_count = argc > 1 ? 
        from_string<max_uint_t>(argv[1]) : 1000000L;
    const std::size_t unique_line_count = argc > 2 ? 
        from_string<std::size_t>(argv[2]) : line_count * 3 / 4;
    const std::size_t max_line_len = argc > 3 ? 
        from_string<std::size_t>(argv[3]) : 100;
    const std::size_t min_line_len = argc > 4 ? 
        from_string<std::size_t>(argv[4]) : 0;

    // Prepare random generator
    const std::string pattern_text = "[a-zA-Z0-9]{"
        + to_string(min_line_len) + "," 
        + to_string(max_line_len) + "}";
    const pattern random_str_generator(pattern_text);

    // Produce the random (almost) unique data
    std::vector<std::string> unique_lines;
    unique_lines.reserve(unique_line_count);
    for (std::size_t i = 0; i != unique_line_count; ++i)
    {
      unique_lines.push_back(random_str_generator.next(rnd));
    }       

    // Produce generated data    
    for (max_uint_t i = line_count; i; --i) 
    {      
      std::cout << unique_lines[rnd.next(unique_line_count)] << std::endl;
    }
    return EXIT_SUCCESS;
  }
  catch (const std::exception& e)
  {
    std::cerr << "Exception: " << e.what() << std::endl;
  }
  catch (...)
  {
    std::cerr << "Unknown exception" << std::endl;
  }
  return EXIT_FAILURE;
}