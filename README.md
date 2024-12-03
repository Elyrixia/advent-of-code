# Advent-of-code

Pushing my solutions for the exercises on https://adventofcode.com/

Took inspirations from https://github.com/scalacenter/scala-advent-of-code for the bootstrap of the project

### Setup

Install https://scala-cli.virtuslab.org/install

Then if you want to work with IntelliJ or VS Code then execute
```
scala-cli setup-ide $YEAR
```
with `$YEAR` being the year you want to work on

### Run an exercice

If you ran the previous scala-cli command for your IDE then you can use the buttons directly from the exercise
 file

If you want to execute through a terminal then execute
```
scala-cli $YEAR -M $DAY.$EXERCISE
```

where `$EXERCISE` is an enumeration which can have the values `example`, `ex1` or `ex2`

So for example a complete command could be
```
scala-cli 2024 -M day1.ex1
```
