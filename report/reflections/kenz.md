# Reflection: Muhammad Kenz s/o Jafar

My part was the tables, staff and console: Table, Staff, Waiter, Restaurant, ConsoleUI and InputReader in Stage 1, showing each customer's type in Stage 2, and the Host in Stage 3. I think my classes ended up small and well tested. WaiterCheck and HostCheck both pass, and the Stage 2 replays I recorded gave exactly the result we planned: 5 paid, $73.50 and an average satisfaction of 76.

What I could have done better is my time. I started late, and to keep up I asked Claude to write a lot of my code, including Restaurant, Waiter and Host. I am sad about that. I can explain the code, but I did not work through most of it myself, and that is how I learn best. It also left me with a question I can't fully answer: AI is incredibly useful for writing code, but if it writes the code and I only study it, at what point does that become cheating? I declared all of it in my paper trail, but the question still bothers me.

Using GitHub as a team was new to me. Branches and pull requests let four of us build one project and merge it together, which worked well. The downside was waiting. I could not record a winning game until the Cashier and the turn loop were merged, so my work was often blocked on someone else's.

On OOP, my own classes taught me the most. Staff holds the one-action-per-turn rule once, so Waiter and Host just inherit it, and Restaurant.seat() is the only place anyone is seated, so the seating rules are never repeated. Polymorphism clicked when ConsoleUI showed VIP and Critic customers through getTypeName() with no instanceof check. I also learned why Host should not extend Waiter: it would inherit takeOrder() and serve(), which breaks the Liskov Substitution Principle.

