# Circles - The Developer Handbook

Hi there! If you're reading this, then you're probably one of the delta devs. And if you're one of the delta devs, you'll probably be writing lots and lots of code. And if you're writing lots and lots of code and haven't read this, you're probably doing it wrong. It's a mathematical certainty.

Have a printer? Print this out and hang it on the ceiling above your bed. Have a phone? Record this as a voice memo and set it as your ring signal, alarm clock and make sure it's the only track in your music app. Have a significant other? Get T-shirts made with different sections of the handbook for each day of the week, and stare at their chest until they feel uncomfortable. Then keep staring. 

In all seriousness, this is supposed to be a reference for anyone working on the project. If a decision has been made about coding style or workflow, you will (at least in theory) find it here. If you find yourself uncertain about this kind of stuff whilst developing, this should be the first place you look for answers.

Happy hacking! :)

## Workflow

### General guidelines
 - **Everything is written in either English or code**
   
   Don't be sloppy; don't write your commit messages entirely in lower case, for example.
 
 - **If you don't think it's good enough, it probably isn't**
   
   Don't send a PR if your code has variables named "assface", or a 5000 line method. Just don't.

- **If you learn something interesting, consider writing a wiki page**

  This is especially true if you think it will be important to the rest of the project. Sharing is caring! :)

- **Being scared of screwing up is much worse than actually screwing up**
   
   A good rule of thumb is, if you haven't killed someone it can be fixed. Unless you're an admin you can't really affect anyone but yourself when screwing up, so the worst that can happen is you lose changes that haven't been pulled into the main repo. Don't be scared to try new things!

### Github is king
Any and all communication relevant to the project that can be made public (including but not limited to questions about the code, the way we do things, bug reports etc.) should go through Github. For example:

 - You're not sure whether you should indent with tabs or spaces, and can't find anything about that in the DH. So, you open an issue stating that the DH is missing this information.
 - You've fixed a bug in production code and are not sure which branches you should merge into once you've fixed it. Again, open an issue.

**Do not** send an email, SMS, or Instagram to the lead developer, project manager or anyone else. There are two reasons for this: one, we hate when you do it, and two, if you use Github then everybody gets to learn from the discussion which means we don't have to write the same thing five times to five different people. Everybody wins :)

Of course, if you want to discuss things "behind closed doors", Github is not the place to do it. Keep it civil.

### How we use Git
Some general guidelines:

 - **Always branch from _develop_ unless you have a _really_ good reason not to**
   
   If you're not sure, branch from develop.

 - **Commit as often as you can**
   
   Hey, it's free right? Seriously, you'll thank yourself for doing it.

 - **Write _descriptive_ commit messages**
   
   If your commit messages read like "Changed some stuff" or "Fixed a bug" your code _will not_ be pulled. I don't care if your code factors integers in polynomial time, it will be completely ignored until you can write a proper commit message.
 
 - **For each task you are assigned, create a new branch.**
   
   Work on that branch, test your code, push to your fork and then send a pull request. _Do not delete the branch until your code has been pulled._ If you are required to make changes to your code, push them to the same branch. After your code has been pulled, you should delete that branch and pull new changes from the upstream repo into your fork.
