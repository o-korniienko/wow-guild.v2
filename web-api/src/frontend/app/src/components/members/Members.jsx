import AppNavbar from './../nav_bar/GeneralNavBar.jsx';
import MembersList from './/MembersList.jsx';
import Stars from './/Stars.jsx';
import {Affix, Card, Input, Layout, Menu, message, Spin} from 'antd';
import 'antd/dist/antd.css';
import './MembersStyle.css';
import {BarChartOutlined, SyncOutlined, TeamOutlined, UnorderedListOutlined} from '@ant-design/icons';
import React, {useEffect, useState} from 'react';
import Cookies from 'universal-cookie';
import {Link} from 'react-router-dom';
import {showError, showErrorAndSetFalse} from './../../common/error-handler.jsx';

const {Search} = Input;
const {Sider, Content} = Layout;
const {Meta} = Card;
const cookies = new Cookies();


const {SubMenu} = Menu;

let MEMBERS = []
let languageLocal;


function isAdmin(user) {
    let roles = user.roles;
    for (var i = 0; i < roles.length; i++) {
        if (roles[i] === "ADMIN") {
            return true;
        }
    }
    return false;
}


const MenuComponent = (props) => {
    const [collapsed, setCollapsed] = useState(true);
    const toggleCollapsed = () => {
        collapsed ? setCollapsed(false) : setCollapsed(true);
    };
    const [hoverList, setHoverList] = useState(false);
    const [hoverUpdateAll, setHoverUpdateAll] = useState(false);
    const [hoverRateList, setHoverRateList] = useState(false);
    const [hoverUpdateLogAll, setHoverUpdateLogAll] = useState(false);


    let language = props.language;
    let updateText = "Update Blizzard Data";
    let starsText = "Rating";
    let memberListText = "Members"
    let listText = "Members List"
    let updateRankText = "Update WOWLogs Data"


    if (language === "UA") {
        updateText = "Оновити Blizzard Дані";
        starsText = "Рейтинг";
        memberListText = "Учасники"
        listText = "Список"
        updateRankText = "Оновити WOWLogs Дані"
    }

    if (props.user !== null && !isAdmin(props.user)) {
        let adminMenu = document.getElementById('admin');
        if (adminMenu != null) {
            adminMenu.style.display = 'none';
        }
    }

    const handleMouseEnter = (index) => {
        switch (index) {
            case 1:
                setHoverList(true);
                ;
                break;
            case 2:
                setHoverUpdateAll(true);
                break;
            case 3:
                setHoverRateList(true);
                break;
            case 4:
                setHoverUpdateLogAll(true);
                break;
            default:

        }
    };

    const handleMouseLeave = (index) => {
        switch (index) {
            case 1:
                setHoverList(false);
                ;
                break;
            case 2:
                setHoverUpdateAll(false);
                break;
            case 3:
                setHoverRateList(false);
                break;
            case 4:
                setHoverUpdateLogAll(false);
                break;
            default:
        }
    };

    const proccessError = (response, mainDiv) => {
        showErrorAndSetFalse(response, props.setLoading);

        if (mainDiv !== null && mainDiv !== undefined){
            mainDiv.className = 'main_div_enabled';
        }
    }

    const updateMembersFromBlizzard = () => {
        props.setLoading(true);

        let mainDiv = document.getElementById('mainDiv');
        if (mainDiv !== null && mainDiv !== undefined) {
            mainDiv.className = 'main_div_disabled';
        }

        fetch("/csrf")
            .then(response => response.status != 200 ? showError(response) :
                response.json())
            .then(data => {
                if (data !== undefined && data !== null && data.token != undefined) {
                    fetch('/member/update-all-bz', {
                        method: 'POST',
                        headers: {
                            'X-XSRF-TOKEN': data.token,
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        credentials: 'include'

                    })
                        .then(response => response.status !== 200 ? proccessError(response, mainDiv) : response.url.includes("login_in") ? window.location.href = "/login_in" : response.json())
                        .then(data => props.setBzData(data));
                }
            });

    }

    const setContent = (value) => {
        window.location.href = "/members/" + value
        // props.setContentType(value)
    }

    const updateRankingData = () => {
        props.setLoading(true);

        let mainDiv = document.getElementById('mainDiv');
        if (mainDiv !== null && mainDiv !== undefined) {
            mainDiv.className = 'main_div_disabled';
        }
        fetch("/csrf")
            .then(response => response.status != 200 ? showError(response) :
                response.json())
            .then(data => {
                if (data !== undefined && data !== null && data.token != undefined) {
                    fetch('/member/update-all-rank', {
                        method: 'POST',
                        headers: {
                            'X-XSRF-TOKEN': data.token,
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        credentials: 'include'

                    })
                        .then(response => response.status !== 200 ? proccessError(response, mainDiv) :
                            response.url.includes("login_in") ? window.location.href = "/login_in" : response.json())
                        .then(data => rankingUpdateResult(data));
                }
            });
    }


    const rankingUpdateResult = (data) => {
        props.setLoading(false);

        if (data !== null && data !== undefined) {
            if (data.message === 'Successful') {
                message.success(data.message)
            } else {
                message.info(data.message)
            }
            let mainDiv = document.getElementById('mainDiv');
            if (mainDiv != null) {
                mainDiv.className = 'main_div_enabled';
            }
        }

    }

    return (<Sider collapsible collapsed={collapsed} onCollapse={toggleCollapsed}>
        <Affix offsetTop={5}>

            <Menu
                mode="inline"

                inlineCollapsed={collapsed}
                style={{height: '100%', borderRight: 0}}
            >


                <SubMenu title={memberListText} icon={<TeamOutlined style={{color: '#1a854f', fontSize: '150%'}}/>}>
                    <Menu.Item key="0" id="admin" icon={<UnorderedListOutlined style={{
                        color: '#1a854f',
                        fontSize: '150%',

                    }}/>}>
                        <Link style={{
                            color: hoverList ? '#1e7ce4' : '#1a854f',
                            fontSize: '100%'
                        }}
                              onClick={() => setContent("list")}
                              onMouseEnter={() => handleMouseEnter(1)}
                              onMouseLeave={() => handleMouseLeave(1)}
                        >
                            {listText}
                        </Link>
                    </Menu.Item>
                    <Menu.Item key="1" id="admin" icon={<SyncOutlined style={{color: '#1a854f', fontSize: '150%'}}/>}>
                        <Link style={{
                            color: hoverUpdateAll ? '#1e7ce4' : '#1a854f',
                            fontSize: '100%'
                        }}
                              onClick={updateMembersFromBlizzard}
                              onMouseEnter={() => handleMouseEnter(2)}
                              onMouseLeave={() => handleMouseLeave(2)}
                        >
                            {updateText}
                        </Link>
                    </Menu.Item>


                    <SubMenu title={starsText} icon={<BarChartOutlined style={{color: '#1a854f', fontSize: '150%'}}/>}>
                        <Menu.Item key="2">
                            <Link style={{
                                color: hoverRateList ? '#1e7ce4' : '#1a854f',
                                fontSize: '100%'
                            }}
                                  onClick={() => setContent("stars")}
                                  onMouseEnter={() => handleMouseEnter(3)}
                                  onMouseLeave={() => handleMouseLeave(3)}
                            >
                                {starsText}
                            </Link>
                        </Menu.Item>
                        <Menu.Item key="3">
                            <Link style={{
                                color: hoverUpdateLogAll ? '#1e7ce4' : '#1a854f',
                                fontSize: '100%'
                            }}
                                  onClick={updateRankingData}
                                  onMouseEnter={() => handleMouseEnter(4)}
                                  onMouseLeave={() => handleMouseLeave(4)}
                            >
                                {updateRankText}
                            </Link>
                        </Menu.Item>

                    </SubMenu>
                </SubMenu>

            </Menu>
        </Affix>
    </Sider>)

}

function Members(props) {
    languageLocal = localStorage.getItem("language") != null ? localStorage.getItem("language") : "EN";
    const [collapsed, setCollapsed] = useState(false);
    const [currentLanguage, setLanguage] = useState(languageLocal);
    const [searchValue, setSearchValue] = useState("");
    const [loading, setLoading] = useState(false);
    const [members, setMembers] = useState(null)
    const [user, setUser] = useState(null);
    const [contentType, setContentType] = useState(props.match.params.tag);
    let patchName = window.location.pathname;

    const setBzData = (value) => {
        setMembers(value);
        MEMBERS = value
        setLoading(false);

        let mainDiv = document.getElementById('mainDiv');
        if (mainDiv != null) {
            mainDiv.className = 'main_div_enabled';
        }
    }

    const ContentDiv = (props) => {

        if (contentType === "list") {
            return (<MembersList onSearch={onSearch} updateCharacterDataInTable={updateCharacterDataInTable}
                                 setMembers={setMembers} setSearchValue={setSearchValue} searchValue={searchValue}
                                 setLoading={setLoading} members={members} allMembers={MEMBERS}
                                 language={currentLanguage}/>)
        }
        if (contentType === "stars") {
            return (<Stars/>)
        }
    }

    const updateCharacterDataInTable = (data, search) => {
        setLoading(false);
        if (data !== null && data !== undefined) {
            if (data.message === 'Successful') {
                message.success(data.message)

                var updatedCharacter = data.data

                for (var i = 0; i < MEMBERS.length; i++) {
                    if (MEMBERS[i].id === updatedCharacter.id) {
                        MEMBERS[i] = updatedCharacter
                    }
                }
            } else {
                message.info(data.message)
            }
            let mainDiv = document.getElementById('mainDiv');

            if (mainDiv != null) {
                mainDiv.className = 'main_div_enabled';
            }

            if (search.trim() === "") {
                setMembers([])
                setMembers(MEMBERS)
            } else {
                onSearch(search)
            }
        }
    }

    useEffect(() => {
        setLoading(true)

        fetch('/user/get-active')
            .then(response => response.status !== 200 ? showErrorAndSetFalse(response, setLoading) : response.url.includes("login_in") ? window.location.href = "/login_in" : response.json())
            .then(data => setUserData(data));

        fetch('/member/get-all', {})
            .then(response => response.status !== 200 ? showError(response) : response.json())
            .then(data => setBzData(data))

    }, []);

    const setUserData = (data) => {
        if (data !== null && data !== undefined) {
            /* if(!isAdmin(data)){
                window.location.href = "/forbidden403"
            }else{
            } */
            setUser(data)
        }
    }

    const isAdmin = (user) => {
        let roles = user.roles;
        for (var i = 0; i < roles.length; i++) {
            if (roles[i] === "ADMIN") {
                return true;
            }
        }

        return false;
    }


    const onSearch = (value) => {
        let str = ""
        if (value.currentTarget != undefined) {
            str = value.currentTarget.value.trim();
        } else {
            str = value.trim();
        }


        if (str === '') {
            setMembers(MEMBERS);

        } else {
            let foundMembers = [];
            for (var i = 0; i < MEMBERS.length; i++) {
                var stringObject = MEMBERS[i].name + " " + MEMBERS[i].classEn + "" + MEMBERS[i].level + "" + MEMBERS[i].rank + "" + MEMBERS[i].race + "" +
                    MEMBERS[i].regionEn;
                if (stringObject.toLowerCase().includes(str.toLowerCase())) {
                    foundMembers.push(MEMBERS[i]);
                }

            }
            setMembers(foundMembers);
        }
        //SEARCH = str
    }


    return (
        <div id="mainDiv">
            <Layout style={{position: "absolute", minHeight: "100%", width: "100%"}}>
                <Spin size="large" spinning={loading}>
                    <AppNavbar style={{height: "10%"}} setFunction={setLanguage} patchName={patchName}/>
                </Spin>
                <Layout>
                    <MenuComponent setContentType={setContentType} user={user} loading={loading}
                                   setSearchValue={setSearchValue} setLoading={setLoading} setBzData={setBzData}
                                   language={currentLanguage}/>
                    <Layout style={{minHeight: "100%"}}>
                        <Content disabled={true}>

                            <ContentDiv contentType={contentType}/>
                        </Content>

                    </Layout>
                </Layout>

            </Layout>
        </div>
    );
}


export default function MembersPage(props) {
    return <Members {...props}/>

}