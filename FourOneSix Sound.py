from firebase import firebase
firebase = firebase.FirebaseApplication('https://fouronesixsound-51999.firebaseio.com/', None)




import pygame
import urllib.request
while 1==1:
    Play = firebase.get('/Play', None)


    print('Welcome to FourOneSix Sound')
    print('')
    print('Now Playing From Firebase Databse')


    if Play==1:

        pygame.mixer.init()
        pygame.mixer.music.load("1536.mp3")
        pygame.mixer.music.set_volume(1.0)
        pygame.mixer.music.play()

        while pygame.mixer.music.get_busy() == True:
            pass
        
        if Play==0:
            pygame.mixer.music.pause()
            
        
        