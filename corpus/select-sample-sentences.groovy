#!/usr/bin/env groovy

def xml = new XmlSlurper().parse('smsCorpus_zh_2015.03.09.xml' as File)

def CITY_PATTERN = ~/北京/
def TEXT_PATTERN = ~/[\p{script=Han}。，？]{3,15}/

def lines = []

xml.message.each{ m ->
	if (m.source.userProfile.city ==~ CITY_PATTERN && m.source.userProfile.nativeSpeaker == 'yes') {
		if (m.text ==~ TEXT_PATTERN) {
			lines += m.text		
		}
	}
}


(1..10).each{ Collections.shuffle(lines) }


lines.take(100).each{ println it }
