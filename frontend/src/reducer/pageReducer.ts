export type PageAction =
  | { type: "SET_PAGE_NUMBER"; payload: number }
  | { type: "INCREMENT_PAGE" }
  | { type: "SET_PAGE_MAX"; payload: number };

interface PageState {
  number: number;
  max: number;
}

export const initialPageState: PageState = {
  number: 0,
  max: 0,
};

export function pageReducer(state: PageState, action: PageAction): PageState {
  switch (action.type) {
    case "SET_PAGE_NUMBER": {
      if (action.payload < 0 || action.payload >= state.max) {
        return state;
      }
      return { ...state, number: action.payload };
    }
    case "INCREMENT_PAGE": {
      if (state.number + 1 >= state.max) {
        return state;
      }
      return { ...state, number: state.number + 1 };
    }
    case "SET_PAGE_MAX": {
      if (action.payload < 0) {
        return state;
      }
      const adjustedNumber =
        state.number > action.payload ? action.payload : state.number;
      return { ...state, max: action.payload, number: adjustedNumber };
    }
    default:
      throw new Error("Ação desconhecida no reducer");
  }
}
