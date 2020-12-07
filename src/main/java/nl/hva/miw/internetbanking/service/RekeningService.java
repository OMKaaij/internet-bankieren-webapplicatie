package nl.hva.miw.internetbanking.service;

import nl.hva.miw.internetbanking.data.dao.RekeningDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RekeningService {

    private Logger logger = LoggerFactory.getLogger(RekeningService.class);
    private RekeningDao rekeningDao;

    @Autowired
    public RekeningService(RekeningDao rekeningDao) {
        this.rekeningDao = rekeningDao;
        logger.warn("New RekeningService.");
    }

//    public Rekening zoekRekOpKlant(Klant klant) {
//        return null;
//    }
}
