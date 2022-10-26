# WebcamPainting
Utilizes a region-growing algorithm to build a webcam paintbrush.

## Overview
This program uses a region-growing algorithm––specifically, flood-fill––to create a paintbrush through the user's webcam. The program begins by opening the user's webcam. The user can then click on any part of the screen to select a "target" color, after which the program will begin the region-growing algorithm, utilizing an implicit queue that cycles through all pixels, to find the largest region of the specified "target" color. This region then becomes the paintbrush, and a white screen replaces the webcam to provide a canvas for the user to paint on.

## Execution
To start the program, simply run CamPaint.java's main method. The keys and their functions are as follows: 'w' – display webcam; 'p' – enable painting; 'r' – display recolored image; 'c' – clear; 'o' – save recolored image; 's' – save the painting
