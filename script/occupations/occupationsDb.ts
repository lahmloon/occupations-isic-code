import {Database, Statement} from "sqlite3";
import {Occupation} from "./data/occupation";

/**
 * Populates DB with data
 */
export async function populate(db: Database, occupations: Array<Occupation>): Promise<void> {
    console.log("Populating Occupations database...");
    await populateOccupations(db, occupations);
}

/**
 * Populates Occupations
 */
 async function populateOccupations(db: Database, occupations: Array<Occupation>) {
    console.log("Populating Occupations table...");
    let validRecords = 0;
    let invalidRecords: Array<number> = [];

    db.serialize();
    await new Promise<void> (((resolve, reject) => {
        db.run(
            "begin transaction",
            function(err: Error) {
                if(null != err) {
                    reject(err);
                } else {
                    resolve();
                }
            }
        )
    }));
    const stmt = await new Promise<Statement> ((resolve, reject) => {
        db.prepare(
            "REPLACE INTO occupations VALUES (?,?,?,?)",
            function(this: Statement, err: Error) {
                if(null != err) {
                    reject(err);
                } else {
                    resolve(this);
                }
            }
        );
    });

    for (const occupation of occupations) {
        //  For check somthing
        await new Promise<void>((resolve, reject) => {
            stmt.run(
                [
                    occupation.id,
                    occupation.id_sub_of.length > 0 ? occupation.id_sub_of : null,
                    occupation.name_th.length > 0 ? occupation.name_th : null,
                    occupation.name_eng.length > 0 ? occupation.name_eng : null,
                ],
                function (err: Error) {
                    if (null != err) {
                        reject(err);
                    } else {
                        validRecords++;
                        resolve();
                    }
                }
            );
        })
    }
    console.log("Occupations populated.");
    console.log(`Valid records: ${validRecords}`);
    console.log(`Invalid records (${invalidRecords.length}):`, invalidRecords);
    await new Promise<void> (((resolve, reject) => {
        stmt.finalize(
            function(err: Error) {
                if(null != err) {
                    reject(err);
                } else {
                    resolve();
                }
            }
        )
    }));
    await new Promise<void> (((resolve, reject) => {
        db.run(
            "commit",
            function(err: Error) {
                if(null != err) {
                    reject(err);
                } else {
                    resolve();
                }
            }
        )
    }));
}

