### 6.0.0 for 1.xx.x (xx.xx.20xx by canitzp):
This update is planned for anytime in 2019 or 2020 and completely changes the base structure and logic of HQM, forever!

### 5.4.0 for 1.12.x (xx.xx.201x by canitzp, duely):
To accomplish this release of HQM we used a special made modpack with a special server to "field test" the mod completely. Most bugs are fixed through that!

- A lot of internal changes, especially changing string uuid instances to proper UUID instances
- **Fixing server bug, that quests are missing after a rejoin or server restart! This was a very often heard, but difficult to reproduce bug**
- Quest sync from a server does now work properly, even if the server quests are updated
- The QDS is now functioning properly again
- Click on a requirement item in the quest books opens the jei recipes for it. thanks duely
- Added TUMAT and TheOneProbe integration (same behaviour as Waila/Hwyla)

### 5.3.5 for 1.12.x (19.12.2018 by canitzp, duely, codewarrior0, TataTawa):
- Some code cleanup and therefor fixing a team creation bug #407 (and several similar issues)
- Added command to toggle edit mode, without game restart! Thanks duely
- Fixing grey tooltip in reward gui. Thanks to codewarrior0 and duely
- Performance increase in large quest books up to 5000%! Thanks TataTawa
- Fixed some null ItemStacks. Thanks duely

### 5.3.4 for 1.12.x (04.10.2018 by canitzp & duely):
- [Support larger quest data sizes by splitting information. thanks to duely](https://github.com/lorddusk/HQM/commit/c2ba8e2c1be5dfea8c25a98223b4696468bead8d)
- Fixed some QDS issues (#396)
- Blind fixed Location Task issue (#400)

### 5.3.3 for 1.12.x (?):
- no record of changes since this version is completely removed from curseforge

### 5.3.2 for 1.12.x (15.08.2018 by canitzp):
- Blind fix #392 and prevented potential bugs
- Fix for reward bag lags and crashes #318
- Fixed Bags are in all Creative Tabs

BETA version since 20.08.2018, cause I got no new issues at GitHub. pls provide every error found, at the issue tracker

### 5.3.1 for 1.12.x (14.08.2018 by canitzp):
- Fixed startup crash due to not obfuscation of methods and fields. I don't know why this sometimes happens

### 5.3.0 for 1.12.x (14.08.2018 by canitzp):
_This version has some serious compiler issues and is not usable. -> 5.3.1_
- General fore and dependences update
- Update Russian language (thx to yaroslav4167)
- Fix #284 (Fluid NPE)
- Blind Fix #275 (Reward Label NPE)
- Blind Fix #377 (Crash on new world start cause ConcurrentModificationException)
- Blind Fix for QuestData syncing error, when too much quests


### 5.2.0 Alpha1 for 1.12.x (24.10.2017 by canitzp):
- Update to all 1.12 versions