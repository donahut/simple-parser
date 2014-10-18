# simple-parser
Author: Thomas Donahue

Email: donahut.se@gmail.com

This was built as part of an assignment for a course, Situated Natural Language Dialogs with Robots, that I took while studying at Tufts University -- Spring 2013. 

## Description

A Clojure library designed to parse sentences using combinatory
categorical grammars. I have utilized a naive breadth first approach,
which is guaranteed to find a valid parse if one is possible (given
the lexicon), though it gets extremely large very fast. For more
infomation on CCG Parsing, see:

http://en.wikipedia.org/wiki/Combinatory_categorial_grammar

## Usage

java -jar simple-parser-0.1.0-standalone.jar [args]

#### arg1 - Sentence 
  
  Choose which sentence to parse. Options below:
  
  1 - "the dog bit John"
  
  2 - "Andie saw Steve"
  
  3 - "the doctor sent for the patient arrived"
  
#### arg2 - Vision 
  
  Choose whether the "vision system" constrains the parse. This only
  works for sentence 2. In the example case, an actual "saw" has been
  identified in vision, and thus constrains the lexicon to remove the
  verb possibility for "saw." You will see no effect from turning on
  vision for sentences 1 and 3. 

0 - Off
1 - On

#### Examples:

java -jar simple-parser-1.0-standalone.jar 1 0

java -jar simple-parser-1.0-standalone.jar 2 1

#### Notes:

What is printed is the entire workspace after each shift and reduce
cycle. For sentence 3, it is virtually impossible to follow what is
going on, but sentences 1 and 2 are manageable. After all of the
workspaces have printed, the total number of stacks on the workspace
is printed. This helps see how the situated "vision" example with
sentence 2 constrains the parse. After this number, a list of the
valid parses is printed. There is an (S) printed for every valid
possible parse, if empty, there were no valid parses. 
