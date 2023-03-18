import { useRouteError } from "react-router-dom";

import AppContainer from "components/AppContainer";
import AppBody from "components/AppBody";
import NavBar from "components/NavBar";

function Error() {
  const error = useRouteError();

  return (
    <AppContainer>
      <NavBar/>
      <AppBody>
        <h1>Ошибочка вышла...</h1>
        <h2>{error.statusText || error.message}</h2>
      </AppBody>
    </AppContainer>
  );
}

export default Error;
