from firebase import firebase
firebase = firebase.FirebaseApplication('https://fouronesixsound-51999.firebaseio.com/', None)

import urllib.request
import pygame
import urllib.request
import decimal
while 1==1:
    Play = firebase.get('/Play', None)   
    url = firebase.get('/URL',None)
    volume = firebase.get('/Volume', None)
    if Play==1:
        urllib.request.urlretrieve(url,'/home/pi/Desktop/mysong.mp3')
        pygame.mixer.init()
        pygame.mixer.music.load("mysong.mp3")
        
        vol = decimal.Decimal(volume)
        Volume = vol * decimal.Decimal('.01')
        
        pygame.mixer.music.set_volume(Volume)
        pygame.mixer.music.play()
        while pygame.mixer.music.get_busy() == True:
            pass