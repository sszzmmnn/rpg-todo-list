# rpg-todo-list

An Android to-do list with a little bit of twist to it: fighting enemies!\
Application made as a part of college course related to programming mobile devices.

### Summary:
- a user gets to create a task
    - when creating the task, the user is asked to give it:
        - a name,
        - level of importance ranging from one to three,
        - is it supposed to be a one-time to-do (doesn't renew), a daily one (gets renewed daily) or a weekly one (gets renewed weekly),
        - time by which the todo will expire.
- created tasks can be ticked off as completed and back,
- only after confirming the to-dos' completion, the user will be rewarded with points, which can be used to improve their "character", which is the game part of the application,
- the mentioned charater gets points for each confirmed to-do, which then can be used to upgrade:
    - strength - dealing more damage to opponents,
    - agility - reduced cooldown between each hit,
    - intelligence - increased chance for a hit to deal double damage,
- each opponent has its own splashart, a name and default health,
- after each opponent gets killed, another opponent (with a slight increase in health) gets randomly assigned,
- the app also allows for checking other players' stats by utilizing a connection to an external API (as long as the player the user is looking for has submitted their stats)
