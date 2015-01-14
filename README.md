de.htwg.wt.fivewins
===================

This is a project for our lecture Webtechnologien. Our task is to develop a WebUI for
an existing game of a former lecture. The Game is written in Java an can be viewed
under [Five Wins](https://github.com/mamawego/de.htwg.se).

## Status
It's ready and all function works, but the code has to be refactored. We're to anxious ;)

## Demo

Want to see how it looks visit the [Live Demo](http://five-wins.herokuapp.com/).

## Used Technologies

We have used a wide technologie stack. 

At the moment used:

- Play Framework
- Bootstrap
- AngularJS
- WebSockets
- Polymer
- Single Sign On
- One Page Scroll
- spin.js

## Own Polymer element
We created an own polymer element. It uses elements from google paper elements. Our polymer element provides our 
local login mask. All you need to insert is:
```
<login-form></login-form>
```

[Link to the code](https://github.com/NicolaiLoeffler/de.htwg.wt.fivewins/blob/master/public/elements/login-form.html)

## Rest-API provided by Play!
| Method | PATH | WHAT |
| ---- | ----- | ----- |
| GET | / | Renders index.scala.html |
| POST | /setCell/:column/:row | Sets cell in column row|
| POST | /game/field | Returns gamefield as Json |
| GET | /about | Returns about page |
| POST | /game/play/:fieldSize | Starts new PVP game |
| POST | /game/play/:fieldSize/NPC/:sign | Starts new NPC game |
| POST | /game/playOnline | Starts online game. |
| GET | /socket | Initialize Websocket |
| POST | /stopGame | Stops game |
| POST | /login/:name/:password | Local login |
| POST | /googlelogin/:mail | Sets session if login is with google |
| GET | /islogedin | Returns if visitor is logged in |
| POST | /logout | Log out player |

To access files from /public/ use /assets/ instead of /public/.

