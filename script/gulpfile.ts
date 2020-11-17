import minimist, {ParsedArgs} from "minimist";
import {occupations} from "./occupations";
import * as path from "path"
import * as fs from "fs"

/**
 * DB schema path
 */
const SCHEMA_PATH = path.join(__dirname, "..", "android", "app", "schemas", "com.lahmloon.occupations_isic_code.occupations.OccupationsDb");
/**
 * DB destination file
 */
const DST_PATH = path.join(__dirname, "..", "android", "app", "src", "main", "assets", "databases", "occupations.db");
/**
 * DB version file
 */
const VERSION_PATH = path.join(__dirname, "..", "android", "app", "gradle.properties");
/**
 * DB version key
 */
const VERSION_KEY = "OCCUPATIONS_DB_VERSION";

/**
 * Known arguments
 */
interface Args extends ParsedArgs {
    dataDir: string
}

/**
 * Increments database version which room uses to guess if new data is available
 * @param file Path to properties
 * @param key Key to set version to
 */
function incrementDatabaseVersion(file: string, key: string) {
    const versionRegex = new RegExp(`${key}\\s*=\\s*(\\d+)`, "gi");
    let contents = fs.readFileSync(file, "utf8");
    const matches = versionRegex.exec(contents);
    if (null === matches) {
        throw new Error("Did not find match in specified file");
    }

    const version = parseInt(matches[1]);
    const newVersion = version + 1;

    console.log("Current version: " + version + ". New version: " + newVersion);

    contents = contents.replace(versionRegex, `${key}=${newVersion}`);

    fs.writeFileSync(file, contents)
}

/**
 * Occupation population
 */
exports.occupations = async () => {
    const options: Args = minimist<Args>(
        process.argv.slice(2),
        {
            string: ["dataDir"],
            default: { dataDir: "_data"}
        }
    );

    // 1. Create database
    const pathToNewDb = await occupations(SCHEMA_PATH, options.dataDir);
    // 2. Copy to assets
    fs.copyFileSync(pathToNewDb, DST_PATH);
    // 3. Increment version
    incrementDatabaseVersion(VERSION_PATH, VERSION_KEY);
};
