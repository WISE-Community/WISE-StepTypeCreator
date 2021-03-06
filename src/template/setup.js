var coreScripts = [
    /*
     * TODO: rename template
     * TODO: rename TemplateNode.js
     * 
     * For example if you are creating a quiz node you would change it to
     * 'vle/node/quiz/QuizNode.js'
     */
	'vle/node/template/TemplateNode.js',
	/*
     * TODO: rename template
     * TODO: rename templateEvents.js
     * 
     * For example if you are creating a quiz node you would change it to
     * 'vle/node/quiz/QuizEvents.js'
	 */
	'vle/node/template/templateEvents.js'
];

var studentVLEScripts = [
	scriptloader.jquerySrc,
	scriptloader.jqueryUISrc,
 	/*
     * TODO: rename template
     * TODO: rename template.js
     * 
     * For example if you are creating a quiz node you would change it to
     * 'vle/node/quiz/quiz.js'
	 */
	'vle/node/template/template.js',
	/*
     * TODO: rename template
     * TODO: rename templateState.js
     * 
     * For example if you are creating a quiz node you would change it to
     * 'vle/node/quiz/quizState.js'
	 */
	'vle/node/template/templateState.js'
];

var authorScripts = [
	/*
	 * TODO: rename template
	 * TODO: rename authorview_template.js
	 * 
	 * For example if you are creating a quiz node you would change it to
	 * 'vle/node/quiz/authorview_quiz.js'
	 */
	'vle/node/template/authorview_template.js'
];

var gradingScripts = [
  	/*
	 * TODO: rename template
	 * TODO: rename templateState.js
	 * 
	 * For example if you are creating a quiz node you would change it to
	 * 'vle/node/quiz/quizState.js'
	 */
	'vle/node/template/templateState.js'
];

var dependencies = [
  	/*
	 * TODO: rename template
	 * TODO: rename TemplateNode.js
	 * 
	 * For example if you are creating a quiz node you would change it to
	 * 'vle/node/quiz/QuizNode.js'
	 */
	{child:"vle/node/template/TemplateNode.js", parent:["vle/node/Node.js"]}
];

/*
 * TODO: rename template
 * For example if you are creating a quiz node and you want to use custom icons,
 * you would change it to 'quiz' and replace the 'template16.png' and 'template28.png'
 * files in the node's 'icons' directory with 'quiz16.png' and 'quiz28.png' 
 * (the icons should be png files with 16x16 and 28x28 pixels respectively)
 * 
 * TODO: rename Template
 * For example if you are creating a quiz node you would change it to
 * 'Quiz'
 * 
 * If you want to provide authors with multiple icon options for this node type,
 * add another entry to the nodeClasses array and add the corresponding icons
 * (using that nodeClass in the filenames) to the 'icons' directory
 */
var nodeClasses = [
	{nodeClass:'template', nodeClassText:'Template'}
];

/*
 * TODO: rename template
 * TODO: rename TemplateNode
 */
var nodeIconPath = 'node/template/icons/';
componentloader.addNodeIconPath('TemplateNode', nodeIconPath);

scriptloader.addScriptToComponent('core', coreScripts);
scriptloader.addScriptToComponent('core_min', coreScripts);

/*
 * TODO: rename template
 * 
 * For example if you are creating a quiz node you would change it to
 * 'quiz'
 */
scriptloader.addScriptToComponent('template', studentVLEScripts);

scriptloader.addScriptToComponent('author', authorScripts);
scriptloader.addScriptToComponent('studentwork', gradingScripts);
scriptloader.addScriptToComponent('studentwork_min', gradingScripts);

scriptloader.addDependencies(dependencies);

/*
 * TODO: rename TemplateNode
 * 
 * For example if you are creating a quiz node you would change it to
 * 'QuizNode'
 */
componentloader.addNodeClasses('TemplateNode', nodeClasses);

/*
 * TODO: rename the file path value
 * 
 * For example if you are creating a quiz node you would change it to
 * 'vle/node/quiz/quiz.css'
 */
var css = [
       	"vle/node/template/template.css"
];

/*
 * TODO: rename template
 * 
 * For example if you are creating a quiz node you would change it to
 * 'quiz'
 */
scriptloader.addCssToComponent('template', css);

var nodeTemplateParams = [
	{
		/*
		 * TODO: rename the file path value
		 * 
		 * For example if you are creating a quiz node you would change it to
		 * 'node/quiz/quizTemplate.qz'
		 */
		nodeTemplateFilePath:'node/template/templateTemplate.te',
		
		/*
		 * TODO: rename the extension value for your step type, the value of the
		 * extension is up to you, we just use it to easily differentiate between
		 * different step type files
		 * 
		 * For example if you are creating a quiz node you would change it to
		 * 'qz'
		 */
		nodeExtension:'te'
	}
];

/*
 * TODO: rename TemplateNode
 * 
 * For example if you are creating a quiz node you would change it to
 * 'QuizNode'
 */
componentloader.addNodeTemplateParams('TemplateNode', nodeTemplateParams);

//used to notify scriptloader that this script has finished loading
if(typeof eventManager != 'undefined'){
	/*
	 * TODO: rename template to your new folder name
	 * 
	 * For example if you were creating a quiz step it would look like
	 * 
	 * eventManager.fire('scriptLoaded', 'vle/node/quiz/setup.js');
	 */
	eventManager.fire('scriptLoaded', 'vle/node/template/setup.js');
};