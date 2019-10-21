import {connect} from "react-redux";
import CheckoutComponent from "../../Component/Checkout/CheckoutComponent";
import {bindActionCreators} from "redux";
import {addItem, empty, saveCartItems} from '../../state/actions';


let mapStateToProps = (state) => {
    return{
        cartlength:state.cart.length,
        items : state.cart,
        userid: state.user.user._id,
        username: state.user.user.firstName,
        userphone: state.user.user.cellPhone,
        useraddr: state.user.user.street//,
        //cartamount: state.cart.cart.amount
        //state.cart.cart
    }
}

let mapDispatchToProps = (dispatch) => {
    return{
        pay: (items,userid)=>{

        }, 
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(CheckoutComponent);