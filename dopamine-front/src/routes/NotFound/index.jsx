import AppContainer from "components/AppContainer";
import AppBody from "components/AppBody";
import NavBar from "components/NavBar";

import "./index.scoped.css";

function Error() {
  return (
    <AppContainer>
      <NavBar/>
      <AppBody>
        <h1><div class="fitter"></div><span>404</span></h1>
        <h2>Меня не существует....</h2>
        <h3>Это происходит не со мной....</h3>
        <h4>Я не здесь....</h4>
        <h4>Я не здесь....</h4>
        <h4>Это не я.....</h4>
        <h4>Я не здесь....</h4>
        <h5>Я не здесь....</h5>
        <h6>Я не здесь....</h6>
        <h6>Я не здесь....</h6>
      </AppBody>
    </AppContainer>
  );
}

export default Error;
 