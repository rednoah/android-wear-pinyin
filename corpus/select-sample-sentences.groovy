#!/usr/bin/env groovy

def xml = new XmlSlurper().parse('smsCorpus_zh_2015.03.09.xml' as File)

// select SMS message from Beijing users that contain between 5 and 15 hanzi
def CITY_PATTERN = ~/北京/
def TEXT_PATTERN = ~/\p{script=Han}{5,15}\p{P}*/

def lines = []

xml.message.each{ m ->
	if (m.source.userProfile.city ==~ CITY_PATTERN && m.source.userProfile.nativeSpeaker == 'yes') {
		if (m.text ==~ TEXT_PATTERN) {
			lines += m.text as String
		}
	}
}


def list = lines.collect{ it.replaceAll(/\p{P}/, '') }.toSet().toList()
(1..10).each{ Collections.shuffle(list, new java.security.SecureRandom()) }


list.take(75).each{ println it }
