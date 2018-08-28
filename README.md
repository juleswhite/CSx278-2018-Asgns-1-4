# Instructions

## Getting the Code

Asgn 1 is contained in the master branch of this repo. Complete this assignment and
make sure that all of your code is committed.

Asgn 2 is contained in the "asgn2" branch of this repo. When you are ready to start
Asgn 2, create a new "asgn2-solution" branch ("git checkout -b asgn2-solution") and
merge the "asgn2" branch into it ("git merge asgn2").

Asgn 3 is contained in the "asgn3" branch of this repo. When you are ready to start
Asgn 3, create a new "asgn3-solution" branch from your asgn2-solution branch ("git
checkout -b asgn3-solution") and merge the "asgn3" branch into it ("git merge asgn3").

Asgn 4 is contained in the "asgn4" branch of this repo. When you are ready to start
Asgn 4, create a new "asgn4-solution" branch from your asgn3-solution branch ("git
checkout -b asgn4-solution") and merge the "asgn4" branch into it ("git merge asgn4").

## Installation for Asgns 1-3

  1. Clone the repo
  2. Install Java 8 or higher
  3. Install Leiningen and ensure it is on your path (https://leiningen.org/)
  4. Install the Amazon AWS CLI Tools (https://aws.amazon.com/cli/) 
  5. Sign up for a Cognito account for the Autograder using:

     https://cs4278-2018.auth.us-east-1.amazoncognito.com/login?response_type=code&client_id=2akam9tnpjfkkdn1e38qbopkb4&redirect_uri=https://www.magnum.io

     Note: after signing up, you may be redirected to a blank page, which is OK. Just
           make sure that you create an account.

  6. Run "lein deps" in the root of the project
  7. Run "lein repl" and make sure that you see something like:

  ```
  nREPL server started on port 55094 on host 127.0.0.1 - nrepl://127.0.0.1:55094
  REPL-y 0.3.7, nREPL 0.2.13
  Clojure 1.9.0
  Java HotSpot(TM) 64-Bit Server VM 1.8.0_131-b11
      Docs: (doc function-name-here)
            (find-doc "part-of-name-here")
    Source: (source function-name-here)
   Javadoc: (javadoc java-object-or-class-here)
      Exit: Control+D or (exit) or (quit)
   Results: Stored in vars *1, *2, *3, an exception in *e

  asgnx.cli=>
  ```

  8. Connect to your REPL from Atom by opening Atom and running the
     command `Proto Repl: Remote Nrepl Connection` and filling in
     `localhost` and the port that was printed out when the repl started.

  9. Once the REPL finishes refreshing, try evaluating:

  ```
  (+ 1 1)
  ```

  10. In another terminal window, run the "lein test-refresh" command at
      the root of the project and make sure that the autograder runs.

  11. Unless this is Asgn 4, skip down to the assignment spec and read
      the rest of this README very carefully.


## Installation for Asgn 4

You must create a Twilio account to send / receive SMS. To do this, follow
these steps:

0. Merge the asgn4 branch into your current branch
1. Install the Serverless framework and all dependencies (NodeJs, etc.): https://serverless.com/framework/docs/providers/aws/guide/installation/
2. Verify that the "sls" command is on your path and add it if it is not
3. Run the command "lein deps" at the root of your project
4. Create a Twilio account and enable 2-factor auth
5. You will need to fund the account with $20

   When you setup billing:

   ```
   WARNING: DO NOT ENABLE AUTO-RECHARGE ON YOUR ACCOUNT

            DO NOT ENABLE AUTO-RECHARGE ON YOUR ACCOUNT

            IF YOU DO, BAD THINGS CAN HAPPEN

            YOU HAVE BEEN WARNED
   ```

   IMPORTANT:
   -----------
   At the end of the class, you should go and release this phone number and
   cancel your Twilio account if you are no longer going to use it.

6. Go to Twilio and buy an SMS-enabled phone number with a 615 area code
   and write down the number.
7. Go to Twilio settings and write down your live and test credentials
8. Run the command "lein deps" in this project
9. Create secure secrets for your Twilio credentials in AWS, by running these
   commands (fill-in <...>):

```
sls secrets set --name twilio-prod-account-sid --text <your live sid> --region us-east-1
sls secrets set --name twilio-prod-token --text <your live token> --region us-east-1
sls secrets set --name twilio-test-account-sid --text <your test sid> --region us-east-1
sls secrets set --name twilio-test-token --text <your test token> --region us-east-1
```

10. Verify that you did everything correctly by running this command
    in the root of the project:

```
sls secrets validate
```

You should see output that looks like this:

```
Serverless: Targeting /..../asgnX/.serverless/asgnx.zip
Serverless: Generating Serverless Secrets Config
Serverless: Validating secrets
Serverless: Secrets validated
```

11. To learn more about serverless secrets management, see: https://github.com/trek10inc/serverless-secrets

12. Run "sls deploy" in the root directory and you should see something like this:

```
Serverless: Targeting /.../asgnX/.serverless/asgnx.zip
Serverless: Generating Serverless Secrets Config
Serverless: Serverless Secrets beginning packaging process
Serverless: Writing .serverless-secrets.json
Serverless: Validating secrets
Serverless: Secrets validated
Serverless: Adding environment variable placeholders for Serverless Secrets
Serverless: Packaging service...
Serverless: Executing "lein update-in :cljs-lambda assoc :functions '[{:name "asgnx-dev-handle-msg" :invoke asgnx.lambda/receive-message}]' -- cljs-lambda build :output /.../asgnX/.serverless/asgnx.zip :quiet"
Serverless: Returning artifact path /.../asgnX/.serverless/asgnx.zip
Serverless: Cleaning up .serverless-secrets.json
Serverless: Uploading CloudFormation file to S3...
Serverless: Uploading artifacts...
Serverless: Validating template...
Serverless: Updating Stack...
Serverless: Checking Stack update progress...
..............
Serverless: Stack update finished...
Service Information
service: asgnx
stage: dev
region: us-east-1
stack: asgnx-dev
api keys:
  None
endpoints:
  GET - https://abcxyz.execute-api.us-east-1.amazonaws.com/dev/msg
  POST - https://defxyz.execute-api.us-east-1.amazonaws.com/dev/msg
functions:
  handle-msg: asgnx-dev-handle-msg
Serverless: Removing old service versions...
```

13. Copy the endpoint URL for POST
14. Create a "deploy.edn" file in the root folder of the project
15. Insert a raw Clojure map in the file with keys for your endpoint and
    phone number like this (replace these dummy values with yours!):

```
{:endpoint "https://abcxyz.execute-api.us-east-1.amazonaws.com/dev/msg"
 :phone-number "+1615xxxxxxx"}
```

16. Run the autograder with "lein test-refresh" and hope for this:

```
================================================
               Estimated Score:
================================================

|             :test | :score | :out-of |
|-------------------+--------+---------|
|   asgnx.core-test |   10.0 |      10 |
| asgnx.deploy-test |   90.0 |      90 |

Total:  100.00 / 100
================================================
Score submitted.

Your actual score is calculated on the server and
may be different than this score in some circumstances.
The server score is considered the definitive score.

Passed all tests

```

## Assignment Spec

These assignments are going to build the basis for a simple text messaging application
to aid students in this course. In Assignment 4, we will deploy this application to
Amazon Web Services and configure an inbound number for receiving text messages. At
this point, you are only building out basic functions that will later be used to
respond to text messages.

All assignments are graded on the basis of passing ALL of the tests for every prior
assignment in addition to any new tests. If you break something that you did in a
prior assignment (e.g., you have a regression), you will lose points. If you don't
finish an assignment, you will need to complete it in order to get full credit
for all subsequent assignments (real-world software development is built on accretion
over time).

**No solutions will be released until after Asgn 3.**

If you do not complete an assignment or fail to get it to work completely, it
is imperative that you ask questions and seek help.

### Asgn 1

Open `src/asgnx/core.cljc` in Atom and look for `Todo:` comments that are for
Asgn 1. Complete all of the todos for the current assignment and leave the
autograder running as you make changes to your code to see your score (see "To run the autograder" below).

### Asgn 2

Open `src/asgnx/core.cljc` in Atom and look for `Todo:`. Complete all of the todos
for the current assignment and leave the autograder running as you edit to see your
score (see "To run the autograder" below). In Asgn 2, you will also need to complete
the `Todo:`s in `src/asgnx/kvstore.cljc`.

### Asgn 3

Starting in Assignment 3, you are going to extend your previous implementation to add
functionality that allows people to pose questions to people that are registered
as an expert on a given topic. You are still responsible for all of the original
functionality (e.g., those tests still have to pass, no regressions allowed) and
the new functionality. Your grade is calculated from all tests from the past
assignments AND all the tests from the new assignment.

The VERY FIRST THING you should do is create empty implementations of these
functions in order to get rid of the compilation errors in core_test.clj:

``` clojure
experts-register
experts-unregister
experts
add-expert
ask-experts
```

For example, you could create the dummy fn:

``` clojure
(defn experts-register [& args])
```

Later, you could modify it to something to match the spec, like:

``` clojure
(defn experts-register [state topic expert-id expert-info]
```


### Asgn 4

This assignment tests your ability to configure and deploy a cloud application
built on Twilio, AWS Lambda, AWS S3, and AWS SSM. Complete the deployment using
the steps above and get the asgnx.deploy-test to pass.

Once you have successfully deployed the application and passed the tests, you
can make changes and redeploy it to AWS by editing the source files and then
running "sls deploy". The build tools will automatically, compile, package,
and upload your code to AWS. The tools will also provision the AWS resources
listed in serverless.yml.

Finally, you should setup your Twilio phone number to delegate SMS to your
Lambda function. To do this, go into Twilio, find your active phone numbers,
select the number, and then set the "A MESSAGE COMES IN" webhook to the
POST endpoint you copied earlier. Make sure "HTTP POST" is listed as the
method after your endpoint.

Finally, submit the "Asgn 4 SMS Integration" quiz on Brightspace so that the
TA can test your SMS integration. Your final score is not produced via autograding
for this piece of the assignment. Your final score is based on the autograding
and verification that your SMS number is working and the integration is
complete.


## Running the Application CLI

The application has been modified to provide a command line interface so that you
can send fake text messages to the system to see how it will respond.

You can invoke the `-main` function from a terminal by running:

```
lein run
```

You should then see the `CSx278` prompt:

```
CSx278:
```

Once you are DONE with Asgn 1, you should be able to type any of the following commands and
see the output:

```
office <monday,tuesday,wednesday,thursday,friday,saturday,sunday>
homepage
welcome <name>
quit

;; After Asgn 3
expert <expert-id> <topic> <expert-info>
ask <topic> <question>
answer <answer>
```

Here is a sample session with a working version of the app:

```clojure
CSx278: homepage
=========================================
  Processing:" homepage " from console_user
  Router: #object[asgnx.core$create_router$fn__13198 0x65031f2 asgnx.core$create_router$fn__13198@65031f2]
  Parsed msg: {:cmd homepage, :args (), :user-id console_user}
  Read state: {}
  Hdlr: #object[asgnx.core$stateless$fn__13140 0x6dad3964 asgnx.core$stateless$fn__13140@6dad3964]
  Hdlr result: [[] https://brightspace.vanderbilt.edu/d2l/home/85892]
  Processing actions: []
  Action results: []
=========================================
out =>  https://brightspace.vanderbilt.edu/d2l/home/85892
CSx278: welcome bob
=========================================
  Processing:" welcome bob " from console_user
  Router: #object[asgnx.core$create_router$fn__13198 0x65036760 asgnx.core$create_router$fn__13198@65036760]
  Parsed msg: {:cmd welcome, :args (bob), :user-id console_user}
  Read state: {}
  Hdlr: #object[asgnx.core$stateless$fn__13140 0x19814318 asgnx.core$stateless$fn__13140@19814318]
  Hdlr result: [[] Welcome bob]
  Processing actions: []
  Action results: []
=========================================
out =>  Welcome bob
CSx278: quit
nil
```
The "CSx278:" is the prompt for you to type a command. The "out =>" is the final output
that was produced. Everything between "=================" is debugging messages that have been added
to help you find errors. You should look at how these messages are printed out and
use the same types of techniques in your own code when you can't figure out why
something isn't working. For now, "println" is your friend. We will use a more
sophisticated logger in later work.


### Running a REPL:

It is extremely useful to create a REPL and connect it to from Atom. This project is preconfigured
with the dependencies needed to connect to your editor.
To start a REPL and evaluate code:

```
lein repl
```

You should see something like this printed out:

```
nREPL server started on port 54404 on host 127.0.0.1 - nrepl://127.0.0.1:54404
REPL-y 0.3.7, nREPL 0.2.13
Clojure 1.9.0
Java HotSpot(TM) 64-Bit Server VM 1.8.0_131-b11
    Docs: (doc function-name-here)
          (find-doc "part-of-name-here")
  Source: (source function-name-here)
 Javadoc: (javadoc java-object-or-class-here)
    Exit: Control+D or (exit) or (quit)
 Results: Stored in vars *1, *2, *3, an exception in *e
```

Once you see this message, open the `src/asgnx/core.clj` file and run the
command `Proto Repl: Remote Nrepl Connection` and filling in `localhost` and
the port that was printed out when the repl started. On Mac, the keymap you were
provided bound this command to "cmd + r". On Windows, it should be bound to
"ctrl + r". You can also invoke it through the command palette with "cmd + m" on
Mac or "ctrl + m" on Windows.  

Leave the REPL running. You do not need to terminate it. If you never close your
REPL, you can connect to it over and over instantly in Atom. If you close your
REPL in the terminal by killing the "lein repl" command, you will have to restart
it each time and it will take longer.

Once connected, you should see a new pane open and then eventually "Refresh complete".
At this point, you can evaluate code in one of three ways:

1. You can type code into the new window and then hit "shift + enter"
2. You can place your cursor after a line of code in a Clojure file and use either
  "cmd + enter" on Mac or "ctrl + enter" on Windows to evaluate the preceding
  line. The evaluation results will be shown inline next to the cursor. Any
  printlns will show up in the REPL window.
3. You can use Protorepl's "Proto Repl: Autoeval File" to continuously execute
   every statement in a file as you type.

The general workflow to experiment with some code is:

1. Open the file to experiment with
2. Use the "Proto Repl: Load Current File" command to evaluate it, which
   is "cmd + l" on Mac and "ctrl + c" on Windows.
3. Type in code and execute it with "cmd + enter" or "ctrl + enter"

## Reading Tests as Specs

The tests in this application serve as a more detailed specification of what you
need to do than the textual descriptions in the comments. It is essential that you
learn to read tests (and later write them). A basic Clojure test has the form:

```clojure
(deftest something-test
  (testing "a message describing the point of the test")
    (is something-that-should-be-true)
    (is something-else-that-should-be-true)
    (is another-thing-that-should-be-true)
```

The basic convention is that tests for the Clojure source file src/foo/xyz.clj
will be contained in a test test/foo/xyz_test.clj. For example, the tests for
src/asgnx/core.cljc are contained in test/asgnx/core_test.clj. You can use this
naming convention when looking for the tests for a bit of code.

For each part of an assignment, you need to find the tests that correspond to
the functions you are working on. Each `is` statement in a test describes
an expected outcome of executing your code. You should read through each `is`
and incrementally read and understand them. After digesting each `is`, update
your code to try and make sure that the statement passes when the autograder
is run.

For example, for Asgn 1, the very first test is the `words-test`:

```clojure
(deftest words-test
  (testing "that sentences can be split into their constituent words"
    (is (= ["a" "b" "c"] (words "a b c")))
    (is (= [] (words "   ")))
    (is (= [] (words nil)))
    (is (= ["a"] (words "a")))
    (is (= ["a"] (words "a ")))
    (is (= ["a" "b"] (words "a b")))))
```

The first `is` statement checks that invoking `(words "a b c")` produces the
output ["a" "b" "c"]. If your code does not produce the expected output, the
test will fail and tell you that the `expected` output was `["a" "b" "c"]` and
that your `actual` output was something else. Each test is a specification for
something that your code has to do in order to receive full credit.

You must read and understand every test. If you are having trouble passing a
test, start a REPL and connect it to your code. Then, copy the part of the
test to the right of the `is` statement and evaluate it (you may need to load
your code and some namespaces). Play around with your code in the REPL until
you can pass the test.

## Running Tests & Grading

The project includes an automated grading system (an autograder) that will test your
code, print your estimated score, and submit the score/code to the instructor. The
grading/submission process is designed to be continuous. You should start the autograder
and leave it running. Each time you make a change to a file, the autograder will be run
and the updated code submitted. You will receive credit for your highest score, even
if you break something and your score goes down -- so make sure and leave the autograder
running!

Most of the time, you want to leave the autograder running. It will automatically execute and
grade all of your code every time that you make a change and show you the output in the terminal.
You can certainly interactively test with a REPL to see what specific lines of code do or aid
in debugging, but the autograder should also be run separately to continuously test how your
code performs.

  1. Open a terminal (or Git Bash Shell) and change to the root of the project
  2. Run `lein test-refresh`
  3. If this is the first time you have run the autograder, it will ask you to login
     using the credentials that you created in Step 5 of the installation instructions.
  4. After logging in successfully, you should see "authenticated as" printed with
     your name / info.
  5. The auto grader will test your code, estimate your score, and submit it to the server.
  6. LEAVE THE AUTOGRADER RUNNING! Late submissions will never be accepted since there is
     no reason not to run the autograder continuously.
  7. After each change you save, you will get feedback on your code to see how your score is
     progressing. You will also not have to worry about forgetting to turn your code in since
     it will happen automatically each time.

  A successful run of the autograder will look like this:

```
*********************************************
*************** Running tests ***************
:reloading (autograder.reporter)
WARNING: name already refers to: #'clojure.core/name in namespace: autograder.reporter, being replaced by: #'autograder.reporter/name
Authenticated as: {:sub ...., :email_verified true, :name ..., :email ..., :username ....}
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

==================================================
             Grading Your Solution
=================================================
Name:  ....
Testing  #'asgnx.core-test/create-router-test
Testing  #'asgnx.core-test/office-hours-for-day-test
Testing  #'asgnx.core-test/formatted-hours-test
Testing  #'asgnx.core-test/cmd-test
Testing  #'asgnx.core-test/handle-message-test
Testing  #'asgnx.core-test/args-test
Testing  #'asgnx.core-test/parsed-msg-test
Testing  #'asgnx.core-test/words-test
================================================
               Estimated Score:
================================================

|           :test | :score | :out-of |
|-----------------+--------+---------|
| asgnx.core-test |  100.0 |     100 |

Total:  100.00 / 100
================================================
Score submitted.

Your actual score is calculated on the server and
may be different than this score in some circumstances.
The server score is considered the definitive score.

Passed all tests
Finished at 12:53:08.788 (run time: 3.386s)

```

## The Definitive Score

After each successful submission, you will see this message:

```
Score submitted.

Your actual score is calculated on the server and
may be different than this score in some circumstances.
The server score is considered the definitive score.
```

Why doesn't the printed score count? Your code changes are all sent to the server
and your actual code is compiled and run against the tests. There are a number of
cases where the code may score higher on your machine than on the server, such as:
1) code that is hardcoded to assumptions on your machine (e.g., it worked on my
machine!); 2) test cases that are accidentally or maliciously changed locally
causing an inflated score; 3) strange compilation / runtime errors that lead to
tests passing when they should not. In all cases, the server-produced score is what
will be used.


## Errors

You may see these types of errors when your code is tested:

  1. Errors - indicate assertions in the tests that are not passing (e.g., implementation issues
     in your code that prevent it from meeting the assignment spec).

     Errors will look like this:

     ```
     Testing  #'asgn1.core-test/foo-test

     ERROR in (foo-test) (core_test.clj:41)
     @Step4
     expected: 1
       actual: (0)
     ```

     The expected value is the correct output. The actual value is what your
     code actually produced as output. If you are confused about a failure, you should
     look at the source file and line number in the `test` folder (e.g., `test/asgn1/core_test.clj`
     line 41 in the foo-test function).

  2. Failures - indicate that your code is throwing unexpected exceptions.

     Failures will look like this:

```
FAIL in (foo-test) (Numbers.java:163)
@Step4

expected: (= 0 (foo [5 6 1 0]))
  actual: #<java.lang.ArithmeticException@62a153d3 java.lang.ArithmeticException: Divide by zero>

                                     clojure.main.main         main.java:   37
                                                   ...                        
                                     clojure.main/main          main.clj:  387
                                     clojure.main/main          main.clj:  424
                                 clojure.main/null-opt          main.clj:  345
                               clojure.main/initialize          main.clj:  311
                                 clojure.main/init-opt          main.clj:  280
                              clojure.main/load-script          main.clj:  278
                                                   ...                        
                                         user/eval4881         REPL Input     
          com.jakemccrary.test-refresh/monitor-project  test_refresh.clj:  255
       com.jakemccrary.test-refresh/monitor-project/fn  test_refresh.clj:  270
                com.jakemccrary.test-refresh/run-tests  test_refresh.clj:  177
       com.jakemccrary.test-refresh/run-selected-tests  test_refresh.clj:  162
com.jakemccrary.test-refresh/suppress-unselected-tests  test_refresh.clj:  125
    com.jakemccrary.test-refresh/run-selected-tests/fn  test_refresh.clj:  164
                                    clojure.core/apply          core.clj:  657
                                                   ...                        
                                clojure.test/run-tests          test.clj:  767 (repeats 2 times)
                                    clojure.core/apply          core.clj:  659
                                                   ...                        
                                   clojure.core/map/fn          core.clj: 2747
                                  clojure.test/test-ns          test.clj:  757
                            clojure.test/test-all-vars          test.clj:  736
                                clojure.test/test-vars          test.clj:  730
                          clojure.test/default-fixture          test.clj:  686
                             clojure.test/test-vars/fn          test.clj:  734
                          clojure.test/default-fixture          test.clj:  686
                          clojure.test/test-vars/fn/fn          test.clj:  734
                                 clojure.test/test-var          test.clj:  716
                              clojure.test/test-var/fn          test.clj:  716
                                    asgn1.core-test/fn     core_test.clj:   41
                                        asgn1.core/foo          core.clj:   40
                                                   ...                        
java.lang.ArithmeticException: Divide by zero
```

A special printer is used to print failures in reverse order of normal stack traces.
The last and probably most relevant line will be at the bottom. If the last line is not
in your code, you should walk sequentially up the list of files/line numbers until you
find code that you wrote. You should start debugging from there. The left column shows
the function that was executing, the middle column shows the file name, and the right
column is the line number.

In this case, the code on line 40 of src/asgn1/core.clj thre a "Divide by zero" exception.


  3. Grading / Submission Errors - your code could not be submitted to the autograding server --
     email the instructor the error message and ensure that you have an Internet connection

     If you don't have an Internet connection, connect to the Internet and try again. You do
     not need to email the instructor in this case.

     A grading / submission error should be rare. Most of the time, these errors will be caused
     when there is not an Internet connection. These types of grading / submission errors will
     be followed by this message:

```
===========================================
               WARNING                     
Unable to submit the assignment for grading.

Please save the ENTIRE expcetion stack
trace above and include it in any emails
to the instructor.
===========================================
```

  4. Compilation Errors - code that doesn't compile gets zero credit

     Compilation errors will look like this:

```
    :error-while-loading asgn1.core

    Error refreshing environment: java.lang.RuntimeException: Map literal must contain an even number of forms, compiling:(asgn1/core.clj:40:14)
    Finished at 12:40:26.351 (run time: 0.142s)     
```

You should go to the file / line number specified (e.g., src/asgn1/core.clj line 40) and fix
the compilation error. No tests will be run and you will get a zero if your code can't be
compiled.


### Bugs

Please let the instructor know ASAP if you encounter assignment or autograder bugs.

### Reading

You should read and understand all of the course reading material that is due before
the assignment due date. In particular, you need to understand:

  Clojure for the Brave and True Through Chapter 3: https://www.braveclojure.com/

Note: The Emacs installation instructions in Clojure for the B&T are not being used. Use the
      leiningen instructions in "Launching a basic Repl".

## Protorepl Keyboard Shortcuts

Mac:
```
cmd + enter = Execute the block of code in front of the cursor
cmd + r     = Connect to a remote REPL
cmd + l     = Load the current file into the REPL
cmd + m     = Open the Atom command palette which can be used to run any  
              command
cmd + p     = Open a file in the current project
cmd + o     = Open a file
```

Windows:
```
ctrl + enter = Execute the block of code in front of the cursor
ctrl + r     = Connect to a remote REPL
ctrl + l     = Load the current file into the REPL
ctrl + m     = Open the Atom command palette which can be used to run any  
              command
ctrl + p     = Open a file in the current project
ctrl + o     = Open a file
```

The typical usage to connect to a REPL would be:

1. Open a Clojure file
2. Make sure you have launched a REPL for the current project in the terminal
3. cmd + r (fill in the info to connect to the repl)
4. cmd + l (load the current file so every function is available)
5. cmd + enter (after some form you want to evaluate / play with)


## Common Exceptions and What they Mean

### Unable to resolve symbol

If you attempt to refer to something that hasn't been defined, such as a method
or variable that doesn't exist, you will get an error like this:

```clojure
java.lang.RuntimeException: Unable to resolve symbol: foo in this context
clojure.lang.Compiler$CompilerException: java.lang.RuntimeException: Unable to resolve symbol: foo in this context, compiling:(/Users/jules/Dev/workspaces/vandy/CS4278-2018-Asgns/asgnX/src/asgnx/core.cljc:1:1)
```

Look at the "Unable to resolve symbol: foo" and figure out what "foo" you are
refering to that hasn't been defined.

Another common reason for these errors is that you are trying to use a function in
another namespace that hasn't been required correctly.

### cannot be cast to clojure.lang.Ifn
A common error is attempting to invoke a function like this:

```clojure
(foo a b)
```

Where `foo` is not a function. For example, this code:

```clojure
(3 1 2)
```

The number `3` is not a function and cannot be invoked. This will produce an
error message that looks like:

```clojure
java.lang.ClassCastException: java.lang.Long cannot be cast to clojure.lang.IFn
```

The error message will vary based on the "type" of the thing that you try to
invoke as a function. For example, trying to invoke a string as a function will
produce:

```clojure
java.lang.ClassCastException: java.lang.String cannot be cast to clojure.lang.IFn
```


## License

Copyright © 2018 Jules White

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
