<template>
    <div>
        <b-modal :id="name" v-model="modalShow" size="sm" hide-footer hide-header title="BootstrapVue">
            <div class="container">
                <div class="confirm-circle">
                    <p style="font-size: 4rem; margin-top: 14px;"><b-icon icon="check2" variant="light"></b-icon></p>
                </div>
                <h2>{{ header }}</h2>
                <p>{{ info }}</p>
                <button @click="closeModal" class="button">{{ buttonText }}</button>
            </div>
        </b-modal> 
    </div>
</template>

<script>
export default {
    name: "ConfirmModal",
    data(){
        return{
            modalShow: this.display
        }
    },
    props: {
        name: String,
        header: String,
        info: String,
        buttonText: String,
        display: Boolean
    },
    methods:{
        closeModal(){
            this.modalShow = false;
        }
    }
}
</script>

<style scoped>

    
    .container{
        font-family: "Mulish";
        width: 100%;
        height: 100%;
        display: flex;
        flex-flow: column;
        align-items: center;
        justify-content: center;
    }

    .confirm-circle{
        width: 100px;
        height: 100px;
        border:solid 2px #81cc34;
        background-color: #81cc34;
        border-radius: 100%;
        margin-bottom: 25px;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0px 0px 3px #616161;
    }

    h2, p {
        margin: 10px;
    }

    .button {
        margin: 20px;
        border-radius: 4px;
        font-size: 16px;
        width: 80%;
        height: 40px;
        color: white;
        background-color: #81cc34;
        border: none;
        transition: 0.2s;
    }

    .button:hover{
        transition: 0.2s;
        background-color: #70b02c;
    }
</style>