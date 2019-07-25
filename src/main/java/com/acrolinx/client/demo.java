


const e = new Endpoint();

const caps = e.getCapabilities();

const exec = execService.createPool(3);
List<Future<Check>> checks = new ();
for(file f : files){
    checks.add(exec.submit( () => { return e.check(f, caps.get()[0]);}));
}

for(Future<Check> c : checks){
    checks.get()
}


############


const e = new Endpoint();

const caps = e.getCapabilities();

List<Future<Check>> checks = new ();
for(file f : files){
    checks.add(e.check(f, caps.get()[0]););
}

for(Future<Check> c : checks){
    checks.get()
}