# Quote Sauce

Quote Sauce est un bot [discord](https://discord.com) qui, à la manière d'un [Pop Sauce](https://jklm.fun), vous fera deviner les identités dérrière des citations plus ou moins connus.
Le préfix du robot est `,` afin de ne pas rentrer en conflit avec d'autres bots.

![](https://cdn.discordapp.com/attachments/555402600640413713/983313129771528232/Quote_Sauce_-_Banniere.png)

## Le système de jeu

Dans un premier temps, un joueur lance une partie grâce à la commande `,start [ID]`.
A ce moment là, les autres joueurs ont **30 secondes** pour rejoindre la partie en cliquand sur un boutton de ce style :

![](https://cdn.discordapp.com/attachments/555402600640413713/985177647652958298/unknown.png)

Au bout des 30 secondes, la partie se lance. Les joueurs sont listés et la première citation apparait.
**Les messages sont automatiquement supprimés pour éviter la triche.**
Si au bout d'une minute les joueurs n'ont pas trouver l'origine de la citation, aucun point n'est donné.
Les citations s'enchainent et un classement est révélé à la fin.

## Le système de thèmes

Pour démmarer une partie, il faut entrer une ID d'un thème.
Tous les thèmes disponibles sont affichable avec la commande `,themeList` ou `,tl`.
Chaque thème est créer manuelement sous format `YAML`.
Plus d'informations sur le [wiki](https://github.com/DjRedstone/quote-sauce/wiki/Thèmes#comment-créer-un-thème-).