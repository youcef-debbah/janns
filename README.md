## Overview

Right after graduating from the university, I became interested in the artificial intelligence field (mainly due to the hype of Google releasing TensorFlow - 1.0.0), after a lot of reading about the subject, instead of just learning how to use a library like TensorFlow I created this project as a sandbox to experiment with artificial neuron network (ANN) models.

This project contains a small library called "janns" that implements many common neuron network components from scratch in Java, using object-oriented and academic-friendly code, with no complex matrix calculations.

The library package is used by a small game called "jpanzer" which is essentially a self-play game with AI agents, each one of these agents (called Apha and Beta) controls a tank on the board, they can move in 4 directions and shoots, the goal is shooting the opponent but other actions like moving can also give the agents score points, a genetic algorithm is used to evolve the models, the average score of hundreds of rounds is used as the fitness function.

The first generation is completely random and quite boring to watch but later generations usually evolve some interesting behavior such as moving diagonally by alternatively moving 1 pixel in each frame.

![screenshot](https://raw.githubusercontent.com/youcef-debbah/janns/main/screenshots/01.png)

## License

This project is licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0) - see the [LICENSE](LICENSE.txt) file for details.

The Apache License 2.0 is a permissive license that allows users to use, modify, and distribute the software with few restrictions.

### Permissions

- You are free to use, modify, and distribute this software.
- You can distribute your own modified versions without having to share the source code.
- This license allows for the software to be included in proprietary projects.

### Conditions

- You must include a copy of the Apache License 2.0 in any distribution of the software.
- You must state any significant changes made to the original software.
- You must include a notice file with your distribution that provides attribution to the original project.

### Limitations

- This software is provided "as is", without any warranties or conditions.
- The license does not grant you any trademark rights.
- The author is not liable for any damages resulting from the use of the software.

*For a full understanding of your rights and responsibilities, please refer to the [official license](https://www.apache.org/licenses/LICENSE-2.0).*

## Contact Us

If you have any questions or inquiries about this project, please don't hesitate to contact the main developer at [youcef-debbah@hotmail.com](mailto:youcef-debbah@hotmail.com).
