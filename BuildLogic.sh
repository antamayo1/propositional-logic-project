#!/bin/bash

clear

# Show the installation process...
echo "---------------------------------------------------------------------"
echo "BuildLogic.sh sets up LOGIC on your machine with the following steps"
echo "---------------------------------------------------------------------"
echo "1. Check and creation of bin directory."
echo "---------------------------------------------------------------------"

# check and create the bin directory
if [ ! -d "$(pwd)/bin" ]; then
  echo "Message: bin directory does not exist."
  mkdir "$(pwd)/bin"
  echo "Message: bin directory created."
else
  echo "Message: bin directory already exists."
fi

echo
echo "Directory Creation Successful!"
echo "---------------------------------------------------------------------"
echo "2. Compiling Java Files to the bin directory."
echo "---------------------------------------------------------------------"

# compile * Java files to the bin directory
echo "Message: Compiling Java files"
javac -d "$(pwd)/bin" *.java
if [ $? -ne 0 ]; then
  echo
  echo "Compilation failed!"
  read -p "Press Enter to continue..."
  exit 1
fi

echo
echo "Compilation Successful!"
echo "---------------------------------------------------------------------"
echo "3. Creating LOGIC command"
echo "---------------------------------------------------------------------"

# shell script LOGIC.sh 
echo "Message: Creating script as connection to LOGIC"
cat <<EOF > "$(pwd)/bin/LOGIC.sh"
#!/bin/bash
cd "$(pwd)"
java -cp "$(pwd)/bin" Main \$1
EOF
chmod +x "$(pwd)/bin/LOGIC.sh"
echo
echo "LOGIC command created successfully!"
echo "---------------------------------------------------------------------"
echo "4. Setting up LOGIC to PATH"
echo "---------------------------------------------------------------------"

# add the bin directory to the PATH variable
NEW_DIR="$(pwd)/bin"
if [[ ":$PATH:" == *":$NEW_DIR:"* ]]; then
  echo "Message: LOGIC is already installed in PATH."
  echo
  echo "The operation completed successfully."
  echo "---------------------------------------------------------------------"
  echo "Installation Successful!"
  echo "---------------------------------------------------------------------"
  read -p "Press Enter to exit..."
  exit 0
fi

echo "Message: Adding LOGIC directory to PATH variable."
echo
export PATH="$PATH:$NEW_DIR"
echo "export PATH=\"\$PATH:$NEW_DIR\"" >> ~/.bashrc

echo "---------------------------------------------------------------------"
echo "Installation Successful!"
echo "---------------------------------------------------------------------"
echo "Please restart your terminal or run 'source ~/.bashrc' to apply changes in PATH."
read -p "Press Enter to exit..."
