import GlassBackground from "@/components/GlassBackground";
import { useLocation, useNavigate } from "react-router-dom";

function GuestInfo() {
  const location = useLocation();
  const navigate = useNavigate();

  const { username, token } = location.state as {
    username: string;
    token: string;
    backgroundLocation?: {
      pathname: string;
      search: string;
    };
  };

  return (
    <>
      <div>
        <h2>Convidado criado</h2>
        <p>Passe o seguinte link para o convidado acessar o evento</p>
        <p>{`${window.location.origin}/auth/guest/${username}/${token}`}</p>
      </div>
      <GlassBackground onClick={() => navigate(-1)} />
    </>
  );
}

export default GuestInfo;
