# Reflection: Danial

## What I built

I did the entity classes Chef, MenuItem, Menu, Order, Cashier, the Money utility and the OrderStatus enumeration in Stage 1. For Stage 3 I did the ComboMeal entity class, PricingPolicy and the `Menu.withCombos()` method, as well as the design decisions.

## What I learned

I have a clearer understanding of OCP through the `ComboMeal` class extending `MenuItem`. Without this approach, I would have to code if/else statements for every class that involves `MenuItem` just to accommodate a `ComboMeal`. This showed me why extending a class is important, especially when accommodating more updates in the future when code becomes more complex.

## A mistake I made

In `MenuItemCheck`, the check printed `FAIL` for every correct rejection, because it treated a thrown exception as a failure without knowing what I expected. Reading it from the terminal made no sense, because the behaviour was what I expected but it printed `FAIL`. Afterwards, I changed it so that if the test caught the error properly, it would print `OK`, and `FAIL` if it couldn't. I also printed the expected and actual output, so it would be clearer to see what went wrong when I run the check.

## What I would change

If I started the project again, I would create a helper class for testing, because I noticed that much of the testing used repeated code.