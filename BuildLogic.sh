BIN_DIR="bin"
if [ ! -d "$BIN_DIR" ]; then
  mkdir "$BIN_DIR"
fi

echo "Compiling Java files..."
javac -d "$BIN_DIR" *.java
if [ $? -ne 0 ]; then
  echo "Compilation failed!"
  echo "Press Enter to continue..."
  read
  exit 1
fi
echo "Compilation successful!"

echo "Creating LOGIC command..."
cat <<EOT > "$BIN_DIR/LOGIC"
#!/bin/bash
java -cp "$BIN_DIR" Main
EOT

chmod +x "$BIN_DIR/LOGIC"
echo "LOGIC command created successfully!"

export PATH="$PWD/$BIN_DIR:$PATH"

echo "To make the LOGIC command permanent, add the following line to your shell profile (.bashrc, .zshrc, etc.):"
echo "export PATH=\"\$PATH:$PWD/$BIN_DIR\""
echo "Setup complete. Use the command: LOGIC sentence.pl"
echo "Press Enter to continue..."
read