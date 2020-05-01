
//关注
function follow(){
     let creator = $("#creator").val();
        $.ajax({
            type: "POST",
            url: "/follow",/*根目录的/collection而不是当前的question/collection*/
            contentType: "application/json",
            data: JSON.stringify({
                "follower": creator
            }),
            success: function (response) {
                if (response.code == 200) {
                    //提交问题后局部刷新
                    $("#follow").load(location.href + " #follow")
                } else {
                    alert(response.message);
                }
            },
            dataType: "json"
        });
}
//收藏
function coll(){
    let contentId = $("#contentId").val();
    $.ajax({
        type: "POST",
        url: "/collection",/*根目录的/collection而不是当前的question/collection*/
        contentType: "application/json",
        data: JSON.stringify({
            "questionId": contentId
        }),
        success: function (response) {
            if (response.code == 200) {
                //提交问题后局部刷新
            $("#collection").load(location.href + " #collection")
            } else {
                alert(response.message);
            }
        },
        dataType: "json"
        });
}
//提交回复
function post() {
    //jquery获取th:value的值
    let contentId = $("#content_id").val();
    //jquery获取textarea的值
    let content = $("#comment_content").val();

    addComment(contentId, 1, content);
}
//添加回复方法
function addComment(targetId, type, content) {
    //评论内容不能为空
    if (!content) {
        alert("回复不能为空哦~");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                // $("#comment_section").hide();//提交问题后隐藏输入框
                //提交问题后自动刷新页面
                window.location.reload();
            } else if (response.code == 2003) {
                //未登录
                let isAccepted = confirm(response.message);
                if (isAccepted) {
                 window.open("http://localhost:8080/");
                 //设置自动登录成功后自动关掉登录页，并回到评论页
                 window.localStorage.setItem("closable", true);
                }
            } else {
                alert(response.message);
            }
        },
        dataType: "json"
    });
}

//添加二级评论
function comment2(e) {
    let commentId = e.getAttribute("data-id");
    let content = $("#input-" + commentId).val();
    addComment(commentId, 2, content);
}
//展开二级评论（待测试：hasClass校验是否有in，不需要额外标记e）
function collapseComments(e) {
    //拿到id
    let id = e.getAttribute("data-id");
    let comments = $("#comment-" + id);
    //获取二级评论展开状态
    let collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //如果已展开，折叠
        comments.removeClass("in");
        e.classList.remove("menu-active");
        e.removeAttribute("data-collapse");
    } else {
        let subCommentContainer = $("#comment-" + id);
        //判断子元素数量决定是否重新写入元素（最后一个元素是回复框，！=1时说明已加载过）
        if (subCommentContainer.children().length != 1) {
            //展开二级评论框
            comments.addClass("in");
            e.classList.add("menu-active");
            //设置评论展开状态
            e.setAttribute("data-collapse", "in");
        } else {
            //获取请求
            $.getJSON("/comment/" + id, function (data) {
                //循环(reverse()改变元素顺序，抵消prepend的影响
                $.each(data.data.reverse(), function (index, comment) {
                    let mediaLeftElement = $("<div/>", {
                        "class": "media-left",
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatar
                    }));
                    let mediaBodyElement = $("<div/>", {
                        "class": "media-body",
                    }).append($("<h6/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    let mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    let commentElement = $("<div/>", {
                        "class": " col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);//prepend是一个一个往上推，按照时间倒序显示会出错
                });
                //展开二级评论框
                comments.addClass("in");
                e.classList.add("menu-active");
                //设置评论展开状态
                e.setAttribute("data-collapse", "in");
            });
        }
    }
}

//标签
function showSelectTag() {
    $("#select-tag").show();
}
function selectTag(e) {
    let value = e.getAttribute("data-tag");
    let previous = $("#tag").val();
    //有这个标签就无法添加
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + '，' + value);
        } else {
            $("#tag").val(value);
        }
    }
}
