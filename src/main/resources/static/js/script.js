$phoneStyle = $('.phone-style');

$('.phone-menu').on('click', function () {
   $('.phone-style').toggleClass('p-active');
   $('i.fa-bars').toggleClass('fa-beat');
});



// Show elements while scrolling down
function Utils() {}
        Utils.prototype = {
            constructor: Utils,
            isElementInView: function (element, fullyInView) {
                var pageTop = $(window).scrollTop();
                var pageBottom = pageTop + $(window).height();
                var elementTop = $(element).offset().top;
                var elementBottom = elementTop + $(element).height();

                if (fullyInView === true) {
                    return ((pageTop < elementTop) && (pageBottom > elementBottom));
                } else {
                    return ((elementTop <= pageBottom) && (elementBottom >= pageTop));
                }
            }
        };

        var Utils = new Utils();
        $(window).on('load', addFadeIn());
        
        $(window).scroll(function() {
            addFadeIn(true);
        });

function addFadeIn(repeat) {
            // var classToFadeIn = ".services-whatcando-ul > li";
            var classToFadeIn = document.querySelectorAll('.will-fadeIn')
            
            $(classToFadeIn).each(function( index ) {
                var isElementInView = Utils.isElementInView($(this), false);
                if (isElementInView) {
                    if(!($(this).hasClass('fadeInRight')) && !($(this).hasClass('fadeInLeft'))) {
                        if(index % 2 == 0) $(this).addClass('fadeInRight');
                        else $(this).addClass('fadeInLeft');
                    }
                } else if(repeat) {
                    $(this).removeClass('fadeInRight');
                    $(this).removeClass('fadeInLeft');
                }
            });
        }


         //Back to top
 $(window).on('scroll', function() {
    if ($(this).scrollTop() >= 500) {
        $('#backtotop').fadeIn(500);
    } else {
        $('#backtotop').fadeOut(500);
    }
});

$('#backtotop').on('click', function() {
    $('body,html').animate({
        scrollTop: 0
    }, 500);
});