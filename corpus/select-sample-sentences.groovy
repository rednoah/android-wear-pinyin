#!/usr/bin/env groovy

def xml = new XmlSlurper().parse('smsCorpus_zh_2015.03.09.xml' as File)

def CITY_PATTERN = ~/北京/
def TEXT_PATTERN = ~/[\p{script=Han}。，？]{3,15}/

xml.message.each{ m ->
	if (m.source.userProfile.city ==~ CITY_PATTERN && m.source.userProfile.nativeSpeaker == 'yes') {
		if (m.text ==~ TEXT_PATTERN) {
			def line = [m.'@id', m.text].join('\t')
			println line			
		}
	}
}
