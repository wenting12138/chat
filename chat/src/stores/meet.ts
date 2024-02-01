import { defineStore } from 'pinia'
import {reactive, ref} from 'vue'
import {
    WebrtcConnection,
} from "@/utils/webrtc";
import apis from '@/services/apis'

type Client = {
    uid: string,
    name: string,
    roomId: string,
    wc: WebrtcConnection,
    muted: boolean,
    view: boolean,
    chat: boolean,
    isSelf: boolean,
    isRoomAdmin: boolean,
    nowStream: string,
}

export const useMeetStore = defineStore('meet', () => {
    const meetInfo = reactive<{
        show: boolean,
    }>({
        show: false,
    })
    const clientList = reactive<Client[]>([])
    const buildSelfClient = (roomData)=>{
        return {
            uid: JSON.parse(localStorage.getItem("USER_INFO")).uid,
            name: roomData.nickname,
            roomId: roomData.roomId,
            wc: new WebrtcConnection(),
            muted: false,
            view: true,
            chat: true,
            isSelf: true,
            isRoomAdmin: true,
            nowStream: 'screen'
        }
    }

    const createRoom = async (curUid, roomData) => {
        console.log("创建会议")
        let meetId = await apis.createMeet(roomData).send();
        roomData.roomId = meetId;
        let client = buildSelfClient(roomData);
        clientList.push(client);
    }

    // 用户加入
    const userJoin = (user)=>{

    }

    // 用户离开
    const userLeave = (user) => {

    }

    return {
        meetInfo,
        createRoom,
        clientList,
        userJoin,
        userLeave
    }
})

