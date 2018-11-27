/* ng-infinite-scroll - v1.0.0 - 2013-02-23 */
var mod;

mod = angular.module('infinite-scroll', []);

mod.directive('infiniteScroll', [
  '$rootScope', '$window', '$timeout', function($rootScope, $window, $timeout) {
    return {
      link: function(scope, elem, attrs) {
        var checkWhenEnabled, handler, scrollDistance, scrollEnabled;
        $window = angular.element($window);
        scrollDistance = 0;
        if (attrs.infiniteScrollDistance != null) {
          scope.$watch(attrs.infiniteScrollDistance, function(value) {
            return scrollDistance = parseInt(value, 10);
          });
        }
        scrollEnabled = true;
        checkWhenEnabled = false;
        if (attrs.infiniteScrollDisabled != null) {
          scope.$watch(attrs.infiniteScrollDisabled, function(value) {
            scrollEnabled = !value;
            if (scrollEnabled && checkWhenEnabled) {
              checkWhenEnabled = false;
              return handler();
            }
          });
        }
        handler = function() {
          var elementBottom, remaining, shouldScroll, windowBottom;
//          windowBottom = $window.height() + $window.scrollTop();
//          elementBottom = elem.offset().top + elem.height();
//          remaining = elementBottom - windowBottom;
          shouldScroll = elem[0].scrollTop+elem.height() >= elem[0].scrollHeight-40;
          if (shouldScroll && scrollEnabled) {
        	  if(elem.find(".loadBottom").length>0){
        		  elem.find(".loadBottom").show();
        	  }else{
        		  elem.append('<div class="mui-pull-bottom-pocket mui-block mui-visibility loadBottom"><div class="mui-pull"><div class="mui-pull-loading mui-icon mui-spinner mui-visibility"></div>'
        					+'<div class="mui-pull-caption mui-pull-caption-refresh">正在加载...</div></div></div>');
        	  }
            if ($rootScope.$$phase) {
              return scope.$eval(attrs.infiniteScroll);
            } else {
              return scope.$apply(attrs.infiniteScroll);
            }
          } else if (shouldScroll) {
            return checkWhenEnabled = true;
          }else if(scrollEnabled){
        	  if(elem.find(".loadBottom").length>0){
        		  elem.find(".loadBottom").hide();
        	  }
          }
        };
        elem.on('scroll', handler);
        scope.$on('$destroy', function() {
          return $window.off('scroll', handler);
        });
        return $timeout((function() {
          if (attrs.infiniteScrollImmediateCheck) {
            if (scope.$eval(attrs.infiniteScrollImmediateCheck)) {
              return handler();
            }
          } else {
            return handler();
          }
        }), 0);
      }
    };
  }
]);
