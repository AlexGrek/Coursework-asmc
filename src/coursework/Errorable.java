/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coursework;

/**
 *  Переменная, константа и еще что-нибудь может содержать ошибку
 *  интерфейс Errorable объеденяет все потенциально содержащие ошибку классы
 */
public interface Errorable {

    /**
     * Возвращает ошибку в команде, или null, если ошибки нет
     * @return ошибка в команде или null
     */
    public String getError();
}
