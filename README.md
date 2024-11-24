# Propositional Logic

A simple implementation of a BNF to create a CLI-like compiler to create truth tables of input propositional sentences.

## Run Locally

Clone the project

```bash
  git clone https://github.com/antamayo1/propositional-logic-project.git
```

### Windows Users

Run ```BuildLogic.bat``` and wait for the compilation to complete, then RESTART your PC to accomodate changes in the PATH.

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

### MacOS Users (STILL NOT TESTED AND USED, DONT USE)

Run ```BuildLogic.sh``` and wait for the compilation to complete.

Note that the exported path is temporary, thus you may run the following in the shell to make it permanent.

zsh users:
``` 
nano ~/.zshrc
export PATH="$PATH:/path/to/your/project/bin"
source ~/.zshrc
```
bash users:
``` 
nano ~/.bashrc
export PATH="$PATH:/path/to/your/project/bin"
source ~/.bashrc
```
Change the path to the path of the bin created by ```BuildLogic.sh```.

Run the following command in the shell:
```
LOGIC sentence.pl
```
The command line will show the following if successful
```
-----------------------------------------------------------------
Welcome to LOGIC sentence.pl
Please Enter 'EXIT' to terminate
-----------------------------------------------------------------
>
```
