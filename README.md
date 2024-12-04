# Propositional Logic

A simple implementation of a BNF to create a CLI-like compiler to create truth tables of input propositional sentences.

## Run Locally

Clone the project

```bash
  git clone https://github.com/antamayo1/propositional-logic-project.git
```

### Windows Users

Run ```BuildLogic.bat``` and wait for the compilation to complete, then **RESTART** your PC to accomodate changes in the PATH.

Now, run command line by searching ```cmd``` and pressing enter.

Run the following command:
```
LOGIC
```
The command line will show the following if successful
```
-----------------------------------------------------------------
Welcome to LOGIC
Please Enter 'EXIT' to terminate
-----------------------------------------------------------------
>
```

The user can also run the program with an input file.
```
P AND Q
P AND (NOT Q)
(P IMPLIES Q) EQUIVALENT ((NOT P) AND Q)
```
saved as ```sentence.pl``` or any file with ```.pl``` extension.

Run the command to process the file. **Note that this will not enter prompt mode.**
```
LOGIC sentence.pl
```
### MacOS Users

1. Suppose the files are downloaded in the folder ```CMSC124```. Open the terminal and ensure that the current working directory is the ```CMSC124``` folder.
```
> cd ~/Documents/CMSC124
```
2. To verify that all files are downloaded and stored in the ```CMSC124``` folder, type the command:
```
> ls -l
```
3. Ensure that Java is installed by typing:
```
> java -version
```
4. Run the shell script ```BuildLogic.sh``` by typing this in the terminal:
```
> bash BuildLogic.sh
```
5. If the script executes successfully, a bin directory will be created. Verify this by typing the com-
mand:
```
> ls -l
```
### Execution
1. Open the terminal and ensure that the current working directory is the ```CMSC124``` folder:
```
> cd ~/Documents/CMSC124
```
2. Change to the bin directory created by the shell script:
```
> cd bin
```
3. To run ```LOGIC.sh``` with a ```<source file>.pl```, type:
```
> ./LOGIC.sh sentence.pl
```
4. In case there was no ```<source file>.pl```, run the executable file ```LOGIC.sh``` by typing:
```
> ./LOGIC.sh
```
5. Input the logic sentence, following the grammar. For example:
```
> P AND Q
```
6. To terminate the program, input ```EXIT```.
