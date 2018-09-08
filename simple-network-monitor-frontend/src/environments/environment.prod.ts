declare function require(moduleName: string): any;

export const environment = {
  production: true,
  url: "//" + window.location.host + "/",
  version: require('../../package.json').version,
  commit: require('../../git.json')['git.commit.id.abbrev']
};
