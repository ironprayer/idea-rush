var socket = new SockJS('/chat');
var stompClient;
let currentPage = 0; // 현재 페이지
const itemsPerPage = 20; // 한 페이지에 보여질 아이템 수
let total;
let inputElement;
let categoryNum;
let pageNum = 0;
// 페이지 로드 시 초기 데이터 불러오기
loadPage(currentPage);
chatting();

function chatting() {
    console.log(localStorage.getItem("authToken"));
    if (localStorage.getItem("authToken") != null) {
        document.getElementById("login").remove();
        document.getElementById("register").remove();

        const logoutButton = document.createElement("button");
        logoutButton.classList.add("button3"); // 적절한 클래스를 추가하세요
        logoutButton.textContent = "Logout"; // 버튼 텍스트 설정
        logoutButton.onclick = function () {
            // 로그아웃 로직을 여기에 추가하세요.
            localStorage.removeItem("authToken")
            location.reload();
        };
        // 로그아웃 버튼을 레이아웃에 추가합니다.
        document.getElementById("layoutb").appendChild(logoutButton);
        document.getElementById("mainImg").style.backgroundImage = "none";
        document.getElementById("mainImg").style.background = "rgba(217, 217, 217, 0.50)";

        //채팅
        const chatMessagesContainer = document.createElement("div");
        chatMessagesContainer.classList.add("chat-messages");
        chatMessagesContainer.id = "chat-room";
        chatMessagesContainer.style.width = "520px"; // 너비 설정
        chatMessagesContainer.style.height = "320px"; // 높이 설정

        const messageInput = document.createElement("input");
        messageInput.type = "text";
        messageInput.id = "messageInput";
        messageInput.placeholder = "메시지 입력";
        messageInput.style.width = "450px"; // 너비 설정
        messageInput.style.float = "left";
        messageInput.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                sendMessage();
            }
        });
        // 전송 버튼 생성
        const sendButton = document.createElement("button");
        sendButton.id = "sendButton";
        sendButton.textContent = "전송";
        sendButton.style.width = "70px"; // 너비 설정
        sendButton.style.float = "left";

        // 생성한 요소를 DOM에 추가
        const chatInputContainer = document.getElementById("mainImg")
        chatInputContainer.append(chatMessagesContainer);
        chatInputContainer.appendChild(messageInput); // 입력란을 컨테이너에 추가합니다.
        chatInputContainer.appendChild(sendButton); // 버튼을 컨테이너에 추가합니다.

        stompClient = Stomp.over(socket);
        var headers = {
            'Authorization': 'Bearer '+localStorage.getItem("authToken") // 여기에 토큰 값을 넣어주세요
        };
        stompClient.connect(headers, onConnectead, onError);

    }
}

function onConnectead() {
    console.log("연결 성공!")
    stompClient.subscribe('/sub', onMessageReceived, {});
}


function onError(error) {
    console.log('error : ' + error);
}


document.getElementById("sendButton").addEventListener("click", function () {
    sendMessage();
});

function sendMessage() {
    const messageInput = document.getElementById("messageInput");
    const messageText = messageInput.value.trim();

    if (messageText !== "") {
        // 입력란 비우기
        messageInput.value = "";

        var chatMessage = {
            name: "익명",
            msg: messageText,
        };
        stompClient.send("/pub/sendMessage", {}, JSON.stringify(chatMessage));

    }
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    const chatMessages = document.getElementById("chat-room");
    // 생성한 메시지를 DOM에 추가

    const messageDiv = document.createElement("div");
    messageDiv.textContent = message.msg;
    messageDiv.style.backgroundColor = "#007bff";
    messageDiv.style.color = "#fff";
    messageDiv.style.padding = "10px";
    messageDiv.style.marginBottom = "5px";
    messageDiv.style.borderRadius = "5px";
    messageDiv.style.width = "250px"; // 너비 설정
    messageDiv.style.marginLeft = "auto"; // 왼쪽 여백을 auto로 설정하여 오른쪽으로 정렬
    chatMessages.appendChild(messageDiv);

    // 스크롤을 가장 아래로 이동하여 최신 메시지를 표시
    chatMessages.scrollTop = chatMessages.scrollHeight;

}


function category(page, category) {
    if (page == null) {
        page = pageNum;
    }
    categoryNum = category;
    document.getElementById("search").value = "";
    console.log(categoryNum, page)
    // Ajax 요청을 보내고 페이지에 따른 데이터를 받아옵니다.
    fetch(`/api/ideas?page=${page}&category=${categoryNum}`)
        .then(response => response.json())
        .then(data => {
            // 데이터를 화면에 표시하고 페이지네이션 업데이트
            displayItems(data);
            updatePagination(data);
        })
        .catch(error => {
            console.error("데이터를 불러오는 중 오류가 발생했습니다:", error.message);
        });
}

function searchPage(page) {
    if (page == null) {
        page = pageNum;
    }
    inputElement = document.getElementById("search").value;
    categoryNum = null;
    // 검색어 가져오기

    // Ajax 요청을 보내고 페이지에 따른 데이터를 받아옵니다.
    fetch(`/api/ideas?page=${page}&keyword=${inputElement}`)
        .then(response => response.json())
        .then(data => {
            // 데이터를 화면에 표시하고 페이지네이션 업데이트
            displayItems(data);
            updatePagination(data);
        })
        .catch(error => {
            console.error("데이터를 불러오는 중 오류가 발생했습니다:", error.message);
        });
}


function loadPage(page) {
    // Ajax 요청을 보내고 페이지에 따른 데이터를 받아옵니다.
    fetch(`/api/ideas?page=${page}`)
        .then(response => response.json())
        .then(data => {
            // 데이터를 화면에 표시하고 페이지네이션 업데이트
            displayItems(data);
            updatePagination(data);
        })
        .catch(error => {
            console.error("데이터를 불러오는 중 오류가 발생했습니다:", error.message);
        });
}

function displayItems(page) {
    const itemList = document.getElementById("itemList");
    // 기존 아이템 목록을 비워줍니다.
    itemList.innerHTML = "";

    const items = page.content; // Page 객체에서 데이터를 가져옴

    items.forEach(item => {
        // 아이템 목록 항목 생성
        const itemContainer = document.createElement("ul");
        itemContainer.classList.add("item-list");

        // 각 아이템을 감싸는 버튼 생성
        const itemButton = document.createElement("button");
        itemButton.classList.add("item-button");
        itemButton.onclick = function () {
            urlEvent('/view/pageDetail?id=' + item.id);
        };

        const itemLi = document.createElement("li");
        itemLi.classList.add("item");

        // 제목 생성 및 설정
        const itemTitle = document.createElement("h2");
        itemTitle.textContent = item.title;

        // 설명 생성 및 설정
        const itemDescription = document.createElement("p");
        itemDescription.textContent = truncateText(item.content, 100);

        // 가격 생성 및 설정
        const itemPrice = document.createElement("span");
        const formattedPrice = formatNumberWithCommas(item.minimumStartingPrice); // 가격 형식화
        itemPrice.textContent = `${formattedPrice} 원`;
        // 버튼에 아이템 컨테이너를 추가
        itemContainer.appendChild(itemButton);
        itemButton.appendChild(itemLi);

        // 아이템 컨테이너에 아이템 항목을 추가
        itemLi.appendChild(itemTitle);
        itemLi.appendChild(itemDescription);
        itemLi.appendChild(itemPrice);

        // 목록 항목을 아이템 목록에 추가
        itemList.appendChild(itemContainer);

    });
}

function formatNumberWithCommas(number) {
    return new Intl.NumberFormat('ko-KR').format(number);
}

function truncateText(text, maxLength) {
    if (text.length > maxLength) {
        return text.substring(0, maxLength) + " ...";
    } else {
        return text;
    }
}

function updatePagination(data) {
    const pageNumbers = document.getElementById("pageNumbers");
    pageNumbers.innerHTML = "";

    const totalPages = data.totalPages;
    total = totalPages;

    // 현재 페이지가 속한 페이지 그룹을 계산합니다.
    const currentPageGroup = Math.ceil((currentPage + 1) / 10);
    const startPage = (currentPageGroup - 1) * 10 + 1;
    const endPage = Math.min(startPage + 9, totalPages);

    for (let i = startPage; i <= endPage; i++) {
        const pageNumberButton = document.createElement("button");
        pageNumberButton.textContent = i;
        pageNumberButton.addEventListener("click", function () {
            currentPage = i - 1;

            if (inputElement != null) {
                searchPage(currentPage)
            } else if (categoryNum != null) {
                category(currentPage, categoryNum)
            } else {
                loadPage(currentPage);
            }
        });
        pageNumberButton.classList.add("page-number")
        // 현재 페이지 버튼인 경우 활성화 표시
        if (i === currentPage + 1) {
            pageNumberButton.classList.add("active");
        }

        pageNumbers.appendChild(pageNumberButton);
    }
}

// "이전" 버튼 이벤트 핸들러
function prevPage() {
    if (currentPage > 0) {
        currentPage--;
        if (inputElement != null) {
            searchPage(currentPage);
        } else if (categoryNum != null) {
            category(currentPage, categoryNum)
        } else {
            loadPage(currentPage);
        }
    }
}


function nextPage() {
    const totalPages = total; // 총 페이지 수

    console.log(inputElement)
    if (currentPage < totalPages - 1) {
        currentPage++;
        if (inputElement != null) {
            searchPage(currentPage);
        } else if (categoryNum != null) {
            category(currentPage, categoryNum)
        } else {
            loadPage(currentPage);
        }
    }
}

function urlEvent(url) {
    window.location.href = url;
}
