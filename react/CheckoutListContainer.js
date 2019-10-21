import {connect} from "react-redux";
import CheckoutList from "../../Component/Checkout/CheckoutListComponent";

const mapStateToProps = (state) => {
    return {
         items: state.cart
    }
}

export default connect(mapStateToProps,null)(CheckoutList);
//subscribing to update state and get the published values from store