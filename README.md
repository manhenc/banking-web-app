# BarbieBank

## References:
- Figma: https://www.figma.com/file/9lG8ytDIPzasemcUoEOh4C/smartBanking?type=design&node-id=7-25&mode=design&t=BBDvoUbjqtRcwJDy-0
- JIRA: https://farin-rahman.atlassian.net/jira/software/projects/FGP/boards/1/backlog
- Diagrams and Documentation: refer to google docs/teams folder


## Getting started
Clone this project in your eclipse-workspace directory

```
cd eclipse-workspace
git clone https://git.fdmgroup.com/jiayuan.ong/barbiebank.git
```

To switch to a branch
```
git checkout <name of the branch>
git pull
```

Do not forget to git pull if you are working on a branch with someone else (to get the latest version)

To push your work to your branch
```
git add .
git commit -m "<message of what you added>"

//to restore branch (revert changes on local but not pushed yet)
git restore .

//if old branch
git push

//if new branch
git push -u origin <name of the branch>
```

If you want to add something to the main branch, you cannot push directly to the main. You need to create a merge request and assign someone else as the approver. If that someone approves, it will be automatically merged to the main. If there are merge conflicts, DO NOT PUSH TO THE MAIN. resolve all your conflicts first and then merge.

Before creating a merge request to the main branch, please ensure the user story you worked on is following the definition of the done (except for the merge to the main branch, since you are still doing it)

## Creating Branches
It would be a very good practice to add a seperate branch for each user story.
For some cases, it is fine to have a branch for multiple user stories.
Indicate your name, the feature you worked on and the user story number on jira for easier reference

**your name**-**feature for this branch**-**task num**

farin-open-bank-account-22

## Project File Structure
#Packages (prefixed with com.fdm.barbiebank)

Please ensure there is each of the following for each entity:
- **model** - to add your entity model 
- **repository** - interface repository
- **controller** - for html rendering 
- **service** - for handling of the repository


Others
- **config** - for configuration, like add resource handlers for image
- **utils** - a method that has been used several times, instead of repeating chunks of code over and over again, just add them into a utils function 
(e.g you need to retrieve user details for most features, declare a method just to retrieve user details in the utils function)


#Folders and Other files
- **templates** - templates that will be used in the web application
- **static** - only for images (please do not add anything that is not an image)


- **application.properties** - DO NOT TOUCH THIS. If you need to change something, discuss with others to see if they are okay with it and the change does not affect their features.
- **log4j2.xml** - for logging purposes


## Definition of Done:
- [ ] Documentation Completed (has clear acceptance criteria and user story)
- [ ] Unit testing written and passed
- [ ] Code working as per UI flow on Figma
- [ ] Passed all the acceptance criteria
- [ ] Successfully integrated to the frontend (check with Jared if needed)
- [ ] Code merged into the Gitlab repository main branch


#For clarifications
Can always ask or check in Telegram/Teams.

