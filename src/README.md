# twitter-story-teller
A Selenium Webdriver script for parsing a .txt file into a series of automated tweets.

<h2>Setup</h2>

It is assumed that you have a working version of either the Eclipse or IntelliJ IDE, software used to open and run Java projects. This is not an executable program so don't expect to get this working right away if you know nothing about Java programming.

<h3>Configuration</h3>

First, you will need to open the <b>commands.properties</b> file in your IDE. This is where you set your Twitter <b>username</b> and <b>password</b>. If you are concerned about security, please don't worry. This script is run only locally on your own machine. In other words, nobody other than yourself will have access to your Twitter username and password. If, on the other hand, you intend to modify this code by running it on a web application, you will probably want to set up some kind of authentication protocol via the Twitter oAuth API so that your users can take advantage of the service safely.

That being said, you can ignore the <b>story</b> parameter in the commands.properties file for now because that is only necessary if you have set a custom path for your input text file. If you would like to do so, see the "Flags" section header below for more information.

<h3>Get Tweeting!</h3>

To get started, simply modify the "story.txt" file at the path twitter-story-teller/src/ with whatever input text you want. The default story is "Marry Had a Little Lamb". Once executed, the script will separate the entire contents of that file into as many 140 character tweets as are necessary to complete your story, all the while tweeting away.

You may also modify the script by setting various flags for common situations. See below:

<h2>Flags</h2>

There are three flags at the top of the main Java class (ValidationClass.java) that will affect how the script is executed. Explanations, and their default values, follow:

	/*
	 * Set this flag to true if you would like to use a custom path for your
	 * input file, i.e. story.txt; next, change the "story" parameter in the
	 * commands.properties file to match your desired path. Otherwise, set this flag
	 * to false if you would like to continue to use the default input file
	 * path, i.e. twitter-story-teller/src/story.txt.
	 */
	static Boolean customStoryPathFlag = false;

	/*
	 * Set this flag to true if your tweets are not safe for work and you would
	 * like to keep your window minimized. Tweets will continue to be automated
	 * in the background. Otherwise, set this flag to false if you don't care.
	 */
	static Boolean tweetsNSFW = true;

<h2>A Fair Warning</h2>
Using this script aggressively will more than likely result in a suspension of your Twitter account. Before executing this script, please familiarize yourself with <b>The Twitter Rules</b> (https://support.twitter.com/articles/18311) and the <b>Twitter limits (API, updates, and following)</b> (https://support.twitter.com/articles/15364) to understand what aggressive usage means in practice. There are other useful policies located in the Twitter Help Center as well. Please do not ignore them.

<h3>Good Example</h3>
A tweetstorm consisting of 10-15 tweets, or about 1500 characters, every once in a while is a reasonable use of this script.

<h3>Bad Example</h3>
Tweeting the entire contents of George R. R. Martin's "A Song of Ice and Fire" in rapid succession is probably not a good idea. Ignoring for a moment the obvious copyright infringement, this will likely get you banned simply by abusing Twitter's user limit on daily post requests.
