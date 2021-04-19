<template>
    <div>
        <b-modal :id="name" v-model="modalShow" size="sm" hide-footer hide-header title="BootstrapVue">
            <div class="container">
                <div class="error-circle">
                    <p style="font-size: 4rem; margin-top: 7px;"><b-icon icon="x" variant="danger"></b-icon></p>
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
    name: "ErrorModal",
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
        text-align: center;
    }

    .error-circle{
        width: 100px;
        height: 100px;
        border:solid 3px #f15e5e;
        border-radius: 100%;
        margin-bottom: 25px;
        display: flex;
        align-items: center;
        justify-content: center;
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
        background-color: #f15e5e;
        border: none;
        transition: 0.2s;
    }

    .button:hover{
        transition: 0.2s;
        background-color: #f15e5e;
    }
</style>