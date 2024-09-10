## Question 1
What are the key things you would consider when creating/consuming an API to ensure that it is secure and reliable?

### Answer:
#### Key points to consider while creating APIs
- Authentication and Authorization: Use strong methods and enforce permissions.
- Input Validation: Prevent injection and ensure data integrity.
- Secure Communication: Use HTTPS and manage certificates.
- Error Handling: Log details securely and provide user-friendly messages.
- Data Protection: Encrypt sensitive data and mask information.
- Versioning: Maintain compatibility and smooth transitions.
- Testing: Testind throughly and scan for vulnerabilities.
- Documentation: Provide clear and accessible documentation.
- Security Headers: Enhance security with HTTP headers.
- Monitoring: Ensure API health and performance.

## Question 2
Theoretical Challenge : CSV file read & write

### Answer:
## - Tackling the Challenge
To solve this problem, the steps would be as follows:
#### Step 1: Parse the CSV
First, read the CSV file and convert it into a structured format, such as a 2D array or object having each row as object and column as fields.
#### Step 2: Detect Formulas & Values
Iterate through each object in the list. If the field contains a formula (e.g., =A2+B2),
If it's a value (e.g., 5 or 7), store the number directly without any further calculation.
#### Step 3: Evaluate Formulas
For each formula, parse and break down the expression.
References: Identify any cell references such as A1, B2, etc.
Mathematical Operations: Parse basic operations such as addition, subtraction, multiplication, and division.
Resolve any cell references by recursively checking the value in the referred cell.
For example, if C1 = 5 + A1, then resolve A1 to its value (e.g., A1 = 5), and compute the formula as 5 + 5 = 10.
#### Step 4: Handle Dependencies
In some cases, one formula may depend on the result of another formula. For instance, in the example:
C3 = C2 + B3
C2 depends on A2 and B2, so C2 should be calculated first before resolving C3.
As per the above approach it will be handled in the top down order
#### Step 5: Output the Results
Once all the formulas have been calculated and all values have been resolved, write the results back to a CSV file.

## - Types of Errors to Check For
- Cyclic Dependencies:
  A formula that depends on itself or creates a circular reference (e.g., A1 = B1 + 1 and B1 = A1 - 1). You would need to detect this and prevent infinite loops.
- Invalid References:
  If a formula refers to a non-existent cell (e.g., A4 when there is no 4th row), it should throw an error.
- Invalid Formulas:
  A formula with invalid syntax, such as missing operators (e.g., =A1 B2) or unsupported functions.
- Data Type Mismatch:
  Ensure that referenced cells contain numeric values when performing arithmetic operations. For instance, trying to calculate =A1 + "abc" would result in an error.
- File Format Issues:
  Ensure that the CSV file is properly formatted. Corrupt or improperly formatted CSV files (e.g., missing commas or extra columns) should be handled gracefully.

## - How a User Might Break the Code

- Circular References:
  Users could introduce circular references that cause the program to loop indefinitely. For example, A1 = B1 + 1 and B1 = A1 - 1 would create an infinite loop unless detected and stopped.
- Unresolved References:
  If a user references cells that don't exist, this could lead to out-of-bounds errors. For example, A4 in a 3-row table would break the program unless checked.
- Complex Formulas:
  Users might provide formulas that are too complex for the parser to handle (e.g., nested parentheses or advanced Excel-like functions such as SUM(), AVERAGE(), etc.). If your parser is only built for basic arithmetic, users providing such formulas could break the program.
- Non-numeric Values in Formulas:
  If a user inserts non-numeric values into cells, such as =A1 + "text", this would cause the program to crash during arithmetic operations unless proper checks are in place.
- CSV File Corruption:
  If the CSV is poorly formatted (e.g., missing values, incorrect column counts, or special characters), the program might not handle this correctly, causing it to crash or output incorrect results.
- Improper Formula Syntax:
  Users might input invalid formula syntax such as =A1++B1 or =A1 B1, which could break the program if the parser doesn't account for all variations.